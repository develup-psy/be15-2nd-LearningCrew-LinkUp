package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserCommentDto;
import com.learningcrew.linkup.linker.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkup.linker.query.dto.query.UserPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityQueryServiceImpl implements CommunityQueryService {
    @Override
    public List<UserPostDto> findPostsByUser(int userId) {
        return List.of();
    }

    @Override
    public List<UserCommentDto> findCommentsByUser(int userId) {
        return List.of();
    }

    @Override
    public UserMannerTemperatureDto getMannerTemperature(int userId) {
        return null;
    }
}
