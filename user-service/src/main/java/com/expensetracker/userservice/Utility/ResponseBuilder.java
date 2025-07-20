package com.expensetracker.userservice.Utility;

import com.expensetracker.userservice.global.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ResponseBuilder {

    public static ResponseDTO buildSuccessResponse(Object responseData, List<Object> dataList, String endpoint) {
        ResponseDTO response = new ResponseDTO();
        response.setResponseData(responseData);
        response.setDataList(dataList);
        response.setLandingTime(Timestamp.valueOf(LocalDateTime.now()));
        response.setEndpoint(endpoint);
        response.setError(null); // No error in success response
        return response;
    }

    public static ResponseDTO buildSuccessResponse(Object responseData, String endpoint) {
        return buildSuccessResponse(responseData, null, endpoint);
    }

    public static ResponseDTO buildErrorResponse(String errorMessage, String errorCode, String endpoint) {
        ResponseDTO response = new ResponseDTO();
        ResponseDTO.ErrorDetails error = new ResponseDTO.ErrorDetails(errorMessage, errorCode);
        response.setResponseData(null);
        response.setDataList(null);
        response.setLandingTime(Timestamp.valueOf(LocalDateTime.now()));
        response.setEndpoint(endpoint);
        response.setError(error);
        return response;
    }
}

