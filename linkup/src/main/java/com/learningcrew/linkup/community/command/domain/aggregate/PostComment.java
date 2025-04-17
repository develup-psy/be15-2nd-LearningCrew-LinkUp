package com.learningcrew.linkup.community.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
@Entity
@Table(name = "community_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private BigInteger commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private int userId;

    @Column(name = "content")
    private String commentContent;

    @Column(name = "is_deleted")
    private String commentIsDeleted = "N";

    @Column(name = "created_at")
    private LocalDateTime postCommentCreatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public PostComment(Post post, int userId, String commentContent) {
        this.post = post;
        this.userId = userId;
        this.commentContent = commentContent;
    }

    public void softDelete() {
        this.commentIsDeleted = "Y";
        this.deletedAt = LocalDateTime.now();
    }

//    public void softDeleteComment(int postId, BigInteger commentId, int userId) {
//    }

    public void setCommentDeletedAt(LocalDateTime now) {
        this.deletedAt = now;
    }
}