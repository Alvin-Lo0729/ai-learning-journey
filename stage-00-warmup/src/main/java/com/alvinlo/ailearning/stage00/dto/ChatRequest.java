package com.alvinlo.ailearning.stage00.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Chat 請求的 DTO。
 *
 * <p>用 Java record 定義不可變的 DTO,搭配 @NotBlank / @Size 做 input validation。
 * Controller 加上 @Valid 才會觸發驗證。
 */
public record ChatRequest(
        @NotBlank(message = "message 不可為空")
        @Size(max = 4000, message = "message 長度不可超過 4000 字元")
        String message
) {
}
