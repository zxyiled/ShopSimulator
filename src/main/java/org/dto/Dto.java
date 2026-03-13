package org.dto;

/**
 * DTOs (Data Transfer Objects) — plain records used as request/response bodies.
 * Jackson serializes/deserializes them automatically.
 */
public class Dto {

    // POST /api/products
    public record RegisterRequest(
            String code,
            String name,
            double price,
            int quantity
    ) {}

    // PATCH /api/products/{code}/stock
    public record StockRequest(
            String operation,   // "augment" | "reduce"
            int quantity
    ) {}

    // GET /api/products/{code}/validate
    public record ValidateRequest(
            int requiredQuantity
    ) {}

    // Generic API response wrapper
    public record ApiResponse<T>(
            boolean success,
            String message,
            T data
    ) {
        public static <T> ApiResponse<T> ok(String message, T data) {
            return new ApiResponse<>(true, message, data);
        }
        public static <T> ApiResponse<T> ok(String message) {
            return new ApiResponse<>(true, message, null);
        }
        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, message, null);
        }
    }
}