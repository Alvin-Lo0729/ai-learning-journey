package com.alvinlo.ailearning.common;

import java.time.Instant;

/**
 * 統一的 API response 格式,後續每個階段的 controller 都可以用這個包裝回應。
 *
 * <p>使用 record 是 Java 14+ 的特性,可以一行定義 immutable 資料類別。
 * 對應你熟悉的 Lombok @Value,但不需要額外依賴。
 *
 * @param <T> response payload 的型別
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        String error,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, Instant.now());
    }
}
