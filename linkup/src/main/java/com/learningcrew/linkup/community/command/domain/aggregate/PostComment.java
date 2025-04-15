package com.learningcrew.linkup.community.command.domain.aggregate;

import com.learningcrew.linkup.community.command.application.dto.PostCommentUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "community_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger postCommentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private int userId;

    private String postCommentContent;

    private String postCommentIsDeleted;

    private LocalDateTime postCommentCreatedAt;

    private LocalDateTime postCommentDeletedAt;

    @PrePersist
    protected void onCreate() {
        this.postCommentCreatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if ("Y".equals(postCommentIsDeleted)) {
            this.postCommentDeletedAt = LocalDateTime.now();
        }
    }

    public void updatePostComment(PostCommentUpdateRequestDTO postCommentUpdateRequestDTO) {
        if (postCommentUpdateRequestDTO.getCommentContent() == null || postCommentUpdateRequestDTO.getCommentContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        this.postCommentContent = postCommentUpdateRequestDTO.getCommentContent();
    }




}