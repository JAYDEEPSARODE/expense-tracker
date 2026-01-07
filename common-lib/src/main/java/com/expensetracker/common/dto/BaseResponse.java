package com.expensetracker.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String endpoint;

    public static <T> BaseResponse<T> success(T data, String endpoint) {
        return new BaseResponse<>(true, "Operation successful", data, LocalDateTime.now(), endpoint);
    }

    public static <T> BaseResponse<T> error(String message, String endpoint) {
        return new BaseResponse<>(false, message, null, LocalDateTime.now(), endpoint);
    }
}
