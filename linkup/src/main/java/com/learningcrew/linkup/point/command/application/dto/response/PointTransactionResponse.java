package com.learningcrew.linkup.point.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointTransactionResponse {
    private String message;
    private int currentPoint;
}
