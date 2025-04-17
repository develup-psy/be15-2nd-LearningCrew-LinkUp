package com.learningcrew.linkup.place.query.service;

import com.learningcrew.linkup.place.query.dto.response.OwnerResponse;

import java.util.List;

public interface OwnerQueryService {
    OwnerResponse getOwnerInfo(int ownerId);
    List<OwnerResponse> findAllOwners();

    List<OwnerResponse> findAllPendedOwners();
}

