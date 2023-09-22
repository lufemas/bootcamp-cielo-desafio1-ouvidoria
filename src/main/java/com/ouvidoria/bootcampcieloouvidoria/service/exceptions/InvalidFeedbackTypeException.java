package com.ouvidoria.bootcampcieloouvidoria.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFeedbackTypeException  extends RuntimeException {
    public InvalidFeedbackTypeException(String type) {
        super("Invalid feedback type " + type);
    }
}
