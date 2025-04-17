package com.learningcrew.linkup.place.query.dto.response;

import com.learningcrew.linkup.place.command.domain.aggregate.entity.Owner;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OwnerResponse {
    private int ownerId;
    private int statusId;
    private String businessRegistrationDocumentUrl;
    private LocalDateTime authorizedAt;
    private String rejectionReason;

    public static OwnerResponse from(Owner owner) {
        return OwnerResponse.builder()
                .ownerId(owner.getOwnerId())
                .statusId(owner.getStatusId())
                .businessRegistrationDocumentUrl(owner.getBusinessRegistrationDocumentUrl())
                .authorizedAt(owner.getAuthorizedAt() != null
                        ? owner.getAuthorizedAt()
                        : null)
                .rejectionReason(owner.getRejectionReason())
                .build();
    }
}
