package com.learningcrew.linkup.community.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostImageResponse {

    private int postId;
    private String message;
}