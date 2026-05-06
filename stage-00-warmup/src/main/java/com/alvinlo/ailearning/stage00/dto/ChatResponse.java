package com.alvinlo.ailearning.stage00.dto;

/**
 * Chat 回應的 DTO。
 *
 * <p>除了 reply 內容,也包含 token 使用量,
 * 讓你看到每次呼叫的成本(這在學習階段很重要,容易看出哪些 prompt 太貴)。
 */
public record ChatResponse(
        String reply,
        Integer inputTokens,
        Integer outputTokens
) {
}
