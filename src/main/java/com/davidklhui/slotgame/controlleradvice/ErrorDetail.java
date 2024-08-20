package com.davidklhui.slotgame.controlleradvice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class ErrorDetail {

    private LocalDateTime dateTime = LocalDateTime.now();
    private HttpStatus status;
    private String message;
    private String detail;

    public String getDateTime(){
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
