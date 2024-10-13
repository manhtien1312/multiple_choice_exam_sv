package com.graduationproject.exam_supervision_server.exception;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotValidException {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<MessageResponse> handleNotValidException(MethodArgumentNotValidException exception){
        String message = exception.getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

}
