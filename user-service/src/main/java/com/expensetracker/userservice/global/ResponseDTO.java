package com.expensetracker.userservice.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private Object responseData;
    private List<Object> dataList;
    private String endpoint;
    private Timestamp landingTime;
    private ErrorDetails error;

    public ResponseDTO(Object responseData, Timestamp landingTime, String endpoint) {
        this.responseData = responseData;
        this.landingTime = landingTime;
        this.endpoint = endpoint;
        this.error = null;
        this.dataList = null;
    }

    @Data
    @AllArgsConstructor
    public static class ErrorDetails {
        private String errorMsg;
        private String errorCode;
    }
}
