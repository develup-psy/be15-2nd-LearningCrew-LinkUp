package com.learningcrew.linkup.community.query.mapper;

import com.learningcrew.linkup.linker.query.dto.query.UserCommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<UserCommentDto> findCommentsByUserId(int userId);
}
