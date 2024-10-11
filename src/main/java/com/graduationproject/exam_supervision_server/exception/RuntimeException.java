package com.graduationproject.exam_supervision_server.exception;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RuntimeException {

    @ExceptionHandler(value = java.lang.RuntimeException.class)
    ResponseEntity<MessageResponse> handleRuntimeException(java.lang.RuntimeException exception){
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }


}
