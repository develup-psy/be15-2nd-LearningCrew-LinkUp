package com.learningcrew.linkup.community.command.application.service;

import com.learningcrew.linkup.common.infrastructure.UserFeignClient;
import com.learningcrew.linkup.community.command.application.dto.PostCreateRequest;
import com.learningcrew.linkup.community.command.application.dto.PostResponse;
import com.learningcrew.linkup.community.command.application.dto.PostUpdateRequest;
import com.learningcrew.linkup.community.command.domain.PostIsNotice;
import com.learningcrew.linkup.community.command.domain.aggregate.Post;
//import com.learningcrew.linkup.community.command.domain.constants.PostIsNotice;
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
    private final UserFeignClient userFeignClient;

    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> postImgs) {

        int userId = postCreateRequest.getUserId();

        // 관리자가 아닌데 공지 등록 시도 → 예외
        if ("Y".equalsIgnoreCase(postCreateRequest.getIsNotice())) {
            throw new BusinessException(ErrorCode.NOT_ADMIN);
        }

        // 매핑
        Post post = modelMapper.map(postCreateRequest, Post.class);

        // Enum으로 직접 세팅
        if (postCreateRequest.getIsNotice() != null) {
            post.setPostIsNotice(PostIsNotice.valueOf(postCreateRequest.getIsNotice().toUpperCase()));
        }

        post.setPostCreatedAt(LocalDateTime.now());
        post.setPostUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostResponse.class);
    }

    @Transactional
    public PostResponse updatePost(int postId, PostUpdateRequest postUpdateRequest, List<MultipartFile> postImgs) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));



        // isNotice 값이 null일 경우 기본값 "N"을 사용하도록 처리
        String isNotice = postUpdateRequest.getIsNotice();
        if (isNotice == null) {
            isNotice = "N";  // 기본값 설정
        }

        // 게시물 업데이트 처리
        post.updatePostDetails(postUpdateRequest.getTitle(), postUpdateRequest.getContent(), isNotice);
        post.setPostUpdatedAt(LocalDateTime.now());

        // 게시물 저장
        Post updatedPost = postRepository.save(post);
        return modelMapper.map(updatedPost, PostResponse.class);
    }

    @Transactional
    public void deletePost(int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.setIsDelete("Y");
        postRepository.save(post);
    }
}