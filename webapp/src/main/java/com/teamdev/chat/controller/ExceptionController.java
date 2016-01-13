package com.teamdev.chat.controller;

import com.teamdev.chat.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Authentication required.")
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException() {

    }

}
