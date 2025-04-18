package com.learningcrew.linkup.community.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostQueryResponse {
    private int postId;
    private String title;
    private String content;
    private String isNotice;
    private String createdAt;
    private String updatedAt;
}




