package com.learningcrew.linkup.community.query.service;

import com.learningcrew.linkup.common.dto.query.UserCommentDto;
import com.learningcrew.linkup.common.dto.query.UserPostDto;

import java.util.List;

public interface CommunityService {

    /* 특정 유저가 작성한 게시글 조회 */
    List<UserPostDto> findPostsByUser(int userId);

    /* 특정 유저가 작성한 댓글 조회 */
    List<UserCommentDto> findCommentsByUser(int userId);
}