package com.learningcrew.linkup.community.command.domain.repository;

import com.learningcrew.linkup.community.command.domain.aggregate.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Post save(Post post);
}