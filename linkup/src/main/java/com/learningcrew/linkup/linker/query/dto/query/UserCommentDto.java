package com.learningcrew.linkup.linker.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "사용자가 작성한 댓글 정보")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentDto {

    @Schema(description = "댓글 ID", example = "202")
    private Long commentId;

    @Schema(description = "댓글이 작성된 게시글 ID", example = "101")
    private Long postId;

    @Schema(description = "댓글 내용", example = "정말 공감돼요!")
    private String content;

    @Schema(description = "댓글 작성일시", example = "2025-04-13T15:00:00")
    private LocalDateTime createdAt;
}

