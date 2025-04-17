package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.client.CommunityClient;
import com.learningcrew.linkupuser.query.dto.query.UserCommentDto;
import com.learningcrew.linkupuser.query.dto.query.UserPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityQueryServiceImpl implements CommunityQueryService {
    private final CommunityClient communityClient;

    /* 작성 게시글 조회 */
    @Override
    public List<UserPostDto> findPostsByUser(int userId) {
        return communityClient.getPostsByUser(userId).getData();
    }

    /* 작성 댓글 조회 */
    @Override
    public List<UserCommentDto> findCommentsByUser(int userId) {
        return communityClient.getCommentsByUser(userId).getData();
    }
}
