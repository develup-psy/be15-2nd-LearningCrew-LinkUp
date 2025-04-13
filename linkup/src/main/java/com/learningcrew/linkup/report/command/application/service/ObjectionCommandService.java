package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.report.command.application.dto.request.CommentObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.request.PostObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.request.ReviewObjectionRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ObjectionRegisterResponse;

public interface ObjectionCommandService {

    ObjectionRegisterResponse submitReviewObjection(Integer reviewId, ReviewObjectionRequest request);

    ObjectionRegisterResponse submitPostObjection(Integer postId, PostObjectionRequest request);

    ObjectionRegisterResponse submitCommentObjection(Long commentId, CommentObjectionRequest request);
}
