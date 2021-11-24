package com.benit.helpworx.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private int httpStatus;
    private LocalDateTime timestamp;
    private String message;

}
