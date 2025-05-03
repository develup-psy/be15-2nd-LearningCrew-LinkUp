package com.learningcrew.linkup.meeting.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchRequest {
    private Integer page = 1;
    private Integer size = 10;

    Integer meetingId;
    Integer reviewerId;
    Integer revieweeId;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
