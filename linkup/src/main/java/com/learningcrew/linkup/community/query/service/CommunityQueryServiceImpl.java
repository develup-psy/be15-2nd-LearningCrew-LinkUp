package com.learningcrew.linkup.community.query.service;


import com.learningcrew.linkup.common.dto.query.UserCommentDto;
import com.learningcrew.linkup.common.dto.query.UserPostDto;
import com.learningcrew.linkup.community.query.mapper.CommentMapper;
import com.learningcrew.linkup.community.query.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityQueryServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;
    private final CommentMapper commentMapper;

    @Override
    public List<UserPostDto> findPostsByUser(int userId) {
        return communityMapper.findPostsByUserId(userId);
    }

    @Override
    public List<UserCommentDto> findCommentsByUser(int userId) {
        return commentMapper.findCommentsByUserId(userId);
    }
}