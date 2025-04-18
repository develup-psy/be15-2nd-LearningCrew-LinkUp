package com.learningcrew.linkup.community.command.domain.repository;

import com.learningcrew.linkup.community.command.domain.aggregate.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    Optional<PostComment> findBycommentIdAndPost_PostId(BigInteger commentId, int postId);
}