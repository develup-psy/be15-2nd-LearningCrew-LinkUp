package com.learningcrew.linkup.linker.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "작성한 게시글 정보")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserPostDto {

    @Schema(description = "게시글 ID", example = "101")
    private int postId;

    @Schema(description = "게시글 제목", example = "이번 모임 후기")
    private String title;

    @Schema(description = "게시글 본문", example = "좋은 시간이었습니다!")
    private String content;

    @Schema(description = "작성일시", example = "2025-04-13T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "좋아요 수", example = "5")
    private int likeCount;

    @Schema(description = "댓글 수", example = "3")
    private int commentCount;
}
