package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.common.query.mapper.RoleMapper;
import com.learningcrew.linkup.community.command.application.dto.PostCreateRequest;
import com.learningcrew.linkup.community.command.application.dto.PostResponse;
import com.learningcrew.linkup.community.command.application.dto.PostUpdateRequest;
import com.learningcrew.linkup.community.command.domain.PostIsNotice;
import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final RoleMapper roleMapper;  // ✅ RoleRepository 대신 RoleMapper 사용

    private boolean isAdmin(int userId) {
        // 사용자 ID를 기준으로 role_id가 1인지 확인
        return roleMapper.findByUserId(userId)
                .map(role -> role.getRoleId() == 1)
                .orElse(false);  // 조회 실패 시 false
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> postImgs) {
        int userId = postCreateRequest.getUserId();

        if ("Y".equalsIgnoreCase(postCreateRequest.getIsNotice()) && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.NOT_ADMIN);
        }

        Post post = modelMapper.map(postCreateRequest, Post.class);

        if (postCreateRequest.getIsNotice() != null) {
            post.setPostIsNotice(PostIsNotice.valueOf(postCreateRequest.getIsNotice().toUpperCase()));
        }

        post.setPostCreatedAt(LocalDateTime.now());
        post.setPostUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostResponse.class);
    }

    @Transactional
    public PostResponse updatePost(int postId, PostUpdateRequest postUpdateRequest, List<MultipartFile> postImgs, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.getUserId() != userId && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        String isNotice = postUpdateRequest.getIsNotice();
        if (isNotice == null) {
            isNotice = "N";
        }

        if ("Y".equalsIgnoreCase(isNotice) && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.NOT_ADMIN);
        }

        post.updatePostDetails(postUpdateRequest.getTitle(), postUpdateRequest.getContent(), isNotice);
        post.setPostUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);
        return modelMapper.map(updatedPost, PostResponse.class);
    }

    @Transactional
    public void deletePost(int postId, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.getUserId() != userId && !isAdmin(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        post.setIsDelete("Y");
        post.setPostDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
