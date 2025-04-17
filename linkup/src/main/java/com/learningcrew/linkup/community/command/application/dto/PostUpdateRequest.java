package com.learningcrew.linkup.community.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {
    private String title;
    private String content;
    private String isNotice;
}