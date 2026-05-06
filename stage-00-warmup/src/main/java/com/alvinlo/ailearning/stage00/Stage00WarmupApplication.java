package com.alvinlo.ailearning.stage00;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 階段 0 的 Spring Boot 啟動類別。
 *
 * <p>啟動方式:
 * <pre>
 *   export ANTHROPIC_API_KEY=sk-ant-xxx
 *   ./mvnw -pl stage-00-warmup spring-boot:run
 * </pre>
 *
 * <p>啟動後可以測試:
 * <pre>
 *   curl -X POST http://localhost:8080/api/chat \
 *        -H "Content-Type: application/json" \
 *        -d '{"message":"用一句話介紹自己"}'
 * </pre>
 */
@SpringBootApplication
public class Stage00WarmupApplication {

    public static void main(String[] args) {
        SpringApplication.run(Stage00WarmupApplication.class, args);
    }
}
