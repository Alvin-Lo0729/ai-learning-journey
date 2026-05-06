package com.alvinlo.ailearning.stage00.controller;

import com.alvinlo.ailearning.common.ApiResponse;
import com.alvinlo.ailearning.stage00.dto.ChatRequest;
import com.alvinlo.ailearning.stage00.dto.ChatResponse;
import com.alvinlo.ailearning.stage00.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Chat 相關的 REST endpoint。
 *
 * <p>提供兩個 endpoint:
 * <ul>
 *   <li>POST /api/chat: 一般同步呼叫,回傳完整內容</li>
 *   <li>POST /api/chat/stream: 串流呼叫,以 Server-Sent Events 回傳</li>
 * </ul>
 *
 * <p>測試指令:
 * <pre>
 *   # 同步
 *   curl -X POST http://localhost:8080/api/chat \
 *        -H "Content-Type: application/json" \
 *        -d '{"message":"用一句話介紹 Spring Boot"}'
 *
 *   # 串流 (-N 表示不要 buffer 輸出)
 *   curl -N -X POST http://localhost:8080/api/chat/stream \
 *        -H "Content-Type: application/json" \
 *        -d '{"message":"從 1 數到 5,每個數字都要說一個有趣的小知識"}'
 * </pre>
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 同步 chat。
     *
     * <p>@Valid 會觸發 ChatRequest 上面的 @NotBlank / @Size 驗證,
     * 失敗時會自動回 400 + 錯誤訊息。
     */
    @PostMapping
    public ApiResponse<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.chat(request.message());
        return ApiResponse.ok(response);
    }

    /**
     * 串流 chat。
     *
     * <p>回傳 {@code text/event-stream} 讓 client 用 SSE 接收。
     * 注意:這個 endpoint 為了示範串流刻意保持簡單,沒有包成 ApiResponse 格式。
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@Valid @RequestBody ChatRequest request) {
        return chatService.chatStream(request.message());
    }
}
