package com.learningcrew.linkup.community.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.community.query.dto.request.CommunitySearchRequest;
import com.learningcrew.linkup.community.query.dto.response.*;
import com.learningcrew.linkup.community.query.mapper.CommentMapper;
import com.learningcrew.linkup.community.query.mapper.PostMapper;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public List<PostQueryResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .filter(post -> post.getIsDeleted().equals("N"))
                .map(post -> {
                    PostQueryResponse dto = new PostQueryResponse();
                    dto.setPostId(post.getPostId());
                    dto.setTitle(post.getTitle());
                    dto.setContent(post.getContent());
                    dto.setIsNotice(post.getPostIsNotice().name());
                    dto.setCreatedAt(post.getPostCreatedAt().toString());
                    dto.setUpdatedAt(post.getPostUpdatedAt().toString());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostListResponse getPostsForUser(CommunitySearchRequest request) {
        List<PostDTO> noticePosts = postMapper.selectNoticePostsForUser(request);
        List<PostDTO> generalPosts = postMapper.selectGeneralPostsForUser(request);
        long total = postMapper.countGeneralPostsForUser(request);

        List<PostDTO> mergedPosts = new ArrayList<>();
        mergedPosts.addAll(noticePosts);
        mergedPosts.addAll(generalPosts);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalPage((int) Math.ceil((double) total / request.getSize()))
                .totalItems(total)
                .build();

        return PostListResponse.builder()
                .posts(mergedPosts)
                .pagination(pagination)
                .build();
    }

    @Transactional(readOnly = true)
    public PostListResponse getPosts(CommunitySearchRequest request) {
        List<PostDTO> posts = postMapper.selectAllPosts(request);
        long total = postMapper.countAllPosts(request);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalPage((int) Math.ceil((double) total / request.getSize()))
                .totalItems(total)
                .build();

        return PostListResponse.builder()
                .posts(posts)
                .pagination(pagination)
                .build();
    }


    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(int postId) {
        PostDetailResponse post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        // 댓글 및 이미지 URL 추가
        List<PostCommentDTO> comments = commentMapper.selectCommentsByPostId(postId);
        List<String> imageUrls = postMapper.selectPostImageUrlsByPostId(postId); // 수정된 부분

        post.setComments(comments);
        post.setImageUrls(imageUrls);

        return post;
    }



    @Transactional(readOnly = true)
    public PostListResponse getPostsByUser(int userId, int page, int size) {
        int offset = (page - 1) * size;

        List<PostDTO> posts = postMapper.selectPostsByUser(userId, offset, size);
        long total = postMapper.countPostsByUser(userId);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) total / size))
                .totalItems(total)
                .build();

        return PostListResponse.builder()
                .posts(posts)
                .pagination(pagination)
                .build();
    }


//    @Transactional(readOnly = true)
//    public PostListResponse getPostsForUser(CommunitySearchRequest request) {
//        List<PostDTO> posts = postMapper.selectAllPostsForUser(request);
//        long total = postMapper.countAllPostsForUser(request);
//
//        Pagination pagination = Pagination.builder()
//                .currentPage(request.getPage())
//                .totalPage((int) Math.ceil((double) total / request.getSize()))
//                .totalItems(total)
//                .build();
//
//        return PostListResponse.builder()
//                .posts(posts)
//                .pagination(pagination)
//                .build();
//    }

}