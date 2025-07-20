package com.expensetracker.userservice.global;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        ResponseDTO response = new ResponseDTO();
        response.setResponseData(null);
        response.setDataList(null);
        response.setLandingTime(Timestamp.valueOf(LocalDateTime.now()));
        response.setEndpoint(request.getRequestURI());
        response.setError(new ResponseDTO.ErrorDetails(ex.getMessage(), "RUNTIME_EXCEPTION"));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        ResponseDTO response = new ResponseDTO();
        response.setResponseData(null);
        response.setDataList(null);
        response.setLandingTime(Timestamp.valueOf(LocalDateTime.now()));
        response.setEndpoint(request.getRequestURI());
        response.setError(new ResponseDTO.ErrorDetails(ex.getMessage(), "400"));

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }
}
