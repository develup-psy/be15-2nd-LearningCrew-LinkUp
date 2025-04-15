package com.learningcrew.linkup.community.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postCommentLikeId;  // 좋아요 ID (자동 증가)

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    private int userId;

}




