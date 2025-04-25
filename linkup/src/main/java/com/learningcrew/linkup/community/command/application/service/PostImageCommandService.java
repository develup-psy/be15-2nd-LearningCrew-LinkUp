package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.common.service.FileStorage;
import com.learningcrew.linkup.community.command.application.dto.PostImageResponse;
import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.aggregate.PostImage;
import com.learningcrew.linkup.community.command.domain.repository.PostImageRepository;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostImageCommandService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FileStorage fileStorage;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Transactional
    public PostImageResponse updatePostImages(int postId, List<MultipartFile> postImgs) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        Post post = postOpt.get();

        // 기존 이미지 삭제 (원하는 로직에 맞게 수정 가능)
        postImageRepository.deleteByPost(post);

        // 새 이미지 리스트를 순회하며 파일 저장 후 post_image 테이블에 삽입
        if (postImgs != null && !postImgs.isEmpty()) {
            for (MultipartFile file : postImgs) {
                if (!file.isEmpty()) {
                    String storedFilename = fileStorage.storeFile(file);
                    PostImage postImage = new PostImage();
                    postImage.setPost(post);
                    postImage.setImageUrl(IMAGE_URL + storedFilename);
                    postImageRepository.save(postImage);
                }
            }
        }

        return PostImageResponse.builder()
                .postId(postId)
                .message("게시물 이미지 업데이트 성공")
                .build();
    }
}