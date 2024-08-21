package com.davidklhui.slotgame.controlleradvice;

import com.davidklhui.slotgame.exception.SlotGameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SlotGameException.class)
    public ResponseEntity<ErrorDetail> handleSymbolException(SlotGameException e, WebRequest request){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

}
