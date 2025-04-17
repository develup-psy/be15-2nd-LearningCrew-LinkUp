package com.learningcrew.linkupuser.query.service;

import com.learningcrew.linkupuser.query.dto.query.UserCommentDto;
import com.learningcrew.linkupuser.query.dto.query.UserPostDto;

import java.util.List;

public interface CommunityQueryService {

    List<UserPostDto> findPostsByUser(int userId);

    List<UserCommentDto> findCommentsByUser(int userId);

}

