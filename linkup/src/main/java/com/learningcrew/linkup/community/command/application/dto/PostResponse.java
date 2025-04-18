package com.learningcrew.linkup.community.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private int postId;
    private String title;
    private String content;
    private String isNotice;
    private String isDeleted;
    private String createdAt;
    private String updatedAt;
}