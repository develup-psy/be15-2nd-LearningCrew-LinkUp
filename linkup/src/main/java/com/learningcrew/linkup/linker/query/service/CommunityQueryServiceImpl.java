package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.community.query.mapper.CommunityMapper;
import com.learningcrew.linkup.linker.query.dto.query.UserCommentDto;
import com.learningcrew.linkup.linker.query.dto.query.UserPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityQueryServiceImpl implements CommunityQueryService {
    private final CommunityMapper communityMapper;

    /* 작성 게시글 조회 */
    @Override
    public List<UserPostDto> findPostsByUser(int userId) {
        return communityMapper.findPostsByUserId(userId);
    }

    /* 작성 댓글 조회 */
    @Override
    public List<UserCommentDto> findCommentsByUser(int userId) {
        return List.of();
    }
}
