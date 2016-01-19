package com.teamdev.chat.controller;

import com.teamdev.chat.exception.AuthenticationException;
import com.teamdev.chat.exception.ChatException;
import com.teamdev.chat.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Authentication required.")
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException() {

    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal server error.")
    @ExceptionHandler(ChatException.class)
    @ResponseBody
    public ErrorResponse handleChatException(ChatException chatException) {
        return new ErrorResponse(chatException.getMessage());
    }

}
