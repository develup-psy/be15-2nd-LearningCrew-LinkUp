package com.learningcrew.linkup.place.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceListRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Integer sportId;
    private String address;

    public int getOffset(){
        return (page - 1) * size;
    }

    public int getLimit(){
        return size;
    }
}
