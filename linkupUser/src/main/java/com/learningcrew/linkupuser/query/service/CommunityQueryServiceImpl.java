package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.query.dto.query.UserCommentDto;
import com.learningcrew.linkupuser.query.dto.query.UserPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityQueryServiceImpl implements CommunityQueryService {
//    private final CommunityMapper communityMapper;
//    private final CommentMapper commentMapper;

    /* 작성 게시글 조회 */
    @Override
    public List<UserPostDto> findPostsByUser(int userId) {
//        return communityMapper.findPostsByUserId(userId);
        return null;
    }

    /* 작성 댓글 조회 */
    @Override
    public List<UserCommentDto> findCommentsByUser(int userId) {
//        return commentMapper.findCommentsByUserId(userId);
        return null;
    }
}
