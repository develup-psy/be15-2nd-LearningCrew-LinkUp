package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.community.command.application.dto.PostCommentCreateRequest;
import com.learningcrew.linkup.community.command.application.dto.PostCommentResponse;
import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.aggregate.PostComment;
import com.learningcrew.linkup.community.command.domain.repository.PostCommentRepository;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

//
//    @Transactional
//    public PostCommentResponse createComment(int postId, PostCommentCreateRequest commentRequest) {
//        // 1. 포스트 존재 여부 확인
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
//
//        // 2. 사용자 조회
//        User user = userRepository.findById(commentRequest.getUserId())
//                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//
//        // 3. 댓글 생성
//        PostComment postComment = new PostComment(post, user.getUserId(), commentRequest.getCommentContent());
//        postComment.setPostCommentCreatedAt(LocalDateTime.now());
//
//        // 4. 저장
//        PostComment savedComment = postCommentRepository.save(postComment);
//
//        // 5. 반환
//        return modelMapper.map(savedComment, PostCommentResponse.class);
//    }
//
//    @Transactional
//    public void DeleteComment(int postId, BigInteger commentId, int userId) {
//
//    }
//
//    public void softDeleteComment(int postId, BigInteger commentId, int userId) {
//        PostComment comment = postCommentRepository.findBycommentIdAndPost_PostId(commentId, postId)
//                .orElseThrow(() -> new BusinessException((ErrorCode.COMMENT_NOT_FOUND)));
//
//        if (comment.getUserId() != userId) {
//            throw new BusinessException((ErrorCode.FORBIDDEN_ACCESS));
//        }
//
//        comment.setCommentIsDeleted("Y");  // soft delete
//        comment.setCommentDeletedAt(LocalDateTime.now());
//
//        postCommentRepository.save(comment);
//    }
}