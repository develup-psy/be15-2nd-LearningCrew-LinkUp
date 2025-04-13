package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserCommentDto;
import com.learningcrew.linkup.linker.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkup.linker.query.dto.query.UserPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityQueryServiceImpl implements CommunityQueryService {
    @Override
    public List<UserPostDto> findPostsByUser(Long userId) {
        return List.of();
    }

    @Override
    public List<UserCommentDto> findCommentsByUser(Long userId) {
        return List.of();
    }

    @Override
    public UserMannerTemperatureDto getMannerTemperature(Long userId) {
        return null;
    }
}
