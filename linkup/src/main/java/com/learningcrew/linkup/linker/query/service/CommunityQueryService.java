package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserCommentDto;
import com.learningcrew.linkup.linker.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkup.linker.query.dto.query.UserPostDto;

import java.util.List;

public interface CommunityQueryService {

    List<UserPostDto> findPostsByUser(Long userId);

    List<UserCommentDto> findCommentsByUser(Long userId);

    UserMannerTemperatureDto getMannerTemperature(Long userId);
}

