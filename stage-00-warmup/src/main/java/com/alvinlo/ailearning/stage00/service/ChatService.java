package com.alvinlo.ailearning.stage00.service;

import com.alvinlo.ailearning.stage00.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.metadata.Usage;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Service;

/**
 * 與 Claude 互動的核心 service。
 *
 * <p>Spring AI 提供兩層抽象:
 * <ul>
 *   <li>{@link ChatModel}: 低階介面,直接呼叫模型</li>
 *   <li>{@link ChatClient}: 高階 fluent API,推薦在應用層使用</li>
 * </ul>
 *
 * <p>這裡示範兩種寫法:
 * <ol>
 *   <li>{@link #chat(String)}: 同步呼叫 (blocking)</li>
 *   <li>{@link #chatStream(String)}: 串流回應 (SSE)</li>
 * </ol>
 *
 * <p>暖身階段刻意不加 system prompt,讓你之後在階段 1 親自體會
 * 「加 system prompt 之前 vs 之後」的輸出差異。
 */
@Service
public class ChatService {

    private final ChatClient chatClient;

    /**
     * Spring AI 自動注入 {@link ChatModel} bean(由 spring-ai-starter-model-anthropic 提供)。
     * 我們在 constructor 把它包成 ChatClient 來用。
     */
    public ChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    /**
     * 同步呼叫:送一句話、等 Claude 回完整內容。
     *
     * <p>適用場景:不需要即時顯示、需要完整內容做後處理(例如解析 JSON)。
     */
    public ChatResponse chat(String userMessage) {
        var response = chatClient.prompt()
                .user(userMessage)
                .call()
                .chatResponse();

        Generation result = response.getResult();
        String reply = result.getOutput().getText();

        // 取得 token 用量(Anthropic 計費依據)
        Usage usage = response.getMetadata().getUsage();
        Integer inputTokens = usage.getPromptTokens();
        Integer outputTokens = usage.getCompletionTokens();

        return new ChatResponse(reply, inputTokens, outputTokens);
    }

    /**
     * 串流呼叫:Claude 邊產生邊送回 chunk。
     *
     * <p>適用場景:聊天 UI、需要立刻顯示內容降低使用者等待感。
     *
     * <p>串流模式無法在過程中精確取得 token 用量(只能在最後一個 chunk 取得),
     * 所以這裡只回傳文字片段。Stage 4 處理 cost 監控時會再回頭強化這塊。
     */
    public Flux<String> chatStream(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .stream()
                .content();
    }
}
