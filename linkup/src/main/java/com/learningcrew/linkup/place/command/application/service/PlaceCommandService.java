package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.common.service.FileStorage;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.place.command.application.dto.request.OperationTimeRequest;
import com.learningcrew.linkup.place.command.application.dto.request.PlaceCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.request.PlaceUpdateRequest;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.OperationTime;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Place;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.PlaceImage;
import com.learningcrew.linkup.place.command.domain.repository.OperationTimeRepository;
import com.learningcrew.linkup.place.command.domain.repository.PlaceImageRepository;
import com.learningcrew.linkup.place.command.domain.repository.PlaceRepository;
import com.learningcrew.linkup.place.query.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceCommandService {

    private final PlaceRepository placeRepository;
    private final OperationTimeRepository operationTimeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final FileStorage fileStorage;
    private final GeocodingService geocodingService;
    private final ModelMapper modelMapper;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    /* 장소 등록 */
    @Transactional
    public int createPlace(PlaceCreateRequest placeCreateRequest, List<MultipartFile> placeImgs) {
        String mainImageFilename = "";

        // 대표 이미지 저장
        if (placeImgs != null && !placeImgs.isEmpty()) {
            MultipartFile mainFile = placeImgs.get(0);
            if (!mainFile.isEmpty()) {
                mainImageFilename = fileStorage.storeFile(mainFile);
            }
        }

        // DTO → Entity
        Place newPlace = modelMapper.map(placeCreateRequest, Place.class);

        geocodingService.getCoordinates(placeCreateRequest.getAddress())
                .ifPresent(coords -> {
                    newPlace.setLatitude(coords[0]);  // 위도
                    newPlace.setLongitude(coords[1]); // 경도
                });

        // 저장 및 생성된 PK 획득
        Place savedPlace = placeRepository.save(newPlace);
        int generatedPlaceId = savedPlace.getPlaceId();

        // 운영 시간 저장
        if (placeCreateRequest.getOperationTimes() != null && !placeCreateRequest.getOperationTimes().isEmpty()) {
            for (OperationTimeRequest opReq : placeCreateRequest.getOperationTimes()) {
                OperationTime opEntity = new OperationTime();
                opEntity.setPlaceId(generatedPlaceId);
                opEntity.setDayOfWeek(opReq.getDayOfWeek());
                opEntity.setStartTime(opReq.getStartTime());
                opEntity.setEndTime(opReq.getEndTime());
                operationTimeRepository.save(opEntity);
            }
        }

        // 이미지 저장
        if (placeImgs != null && !placeImgs.isEmpty()) {
            for (MultipartFile file : placeImgs) {
                if (!file.isEmpty()) {
                    String storedFilename = fileStorage.storeFile(file);
                    PlaceImage placeImage = new PlaceImage();
                    placeImage.setPlaceId(generatedPlaceId);
                    placeImage.setImageUrl(IMAGE_URL + storedFilename);
                    placeImageRepository.save(placeImage);
                }
            }
        }

        return generatedPlaceId;
    }

    /* 장소 수정 */
    @Transactional
    public void updatePlace(int placeId, PlaceUpdateRequest placeUpdateRequest) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        place.updatePlaceDetails(
                placeUpdateRequest.getSportId(),
                placeUpdateRequest.getPlaceName(),
                placeUpdateRequest.getAddress(),
                placeUpdateRequest.getDescription(),
                placeUpdateRequest.getEquipment(),
                placeUpdateRequest.getIsActive(),
                placeUpdateRequest.getRentalCost()
        );

        geocodingService.getCoordinates(placeUpdateRequest.getAddress())
                .ifPresent(coords -> {
                    place.setLatitude(coords[0]);
                    place.setLongitude(coords[1]);
                });
    }
}
