package com.whaa.shop.common.api;

public record ApiResponse<T>(boolean success, T data, String message) {
    public static <T> ApiResponse<T> ok(T data) { return new ApiResponse<>(true, data, "success"); }
    public static ApiResponse<Void> ok() { return new ApiResponse<>(true, null, "success"); }
    public static ApiResponse<Void> fail(String message) { return new ApiResponse<>(false, null, message); }
}

