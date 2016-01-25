package com.teamdev.chat.controller;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamdev.chat.exception.AuthenticationException;
import com.teamdev.chat.exception.ChatException;
import com.teamdev.chat.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public void handleAuthenticationException(AuthenticationException authenticationException, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendJsonMessage(authenticationException.getMessage(), HttpStatus.FORBIDDEN.value(), response);
    }

    @ExceptionHandler(ChatException.class)
    @ResponseBody
    public void handleChatException(ChatException chatException, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendJsonMessage(chatException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), response);
    }


    private void sendJsonMessage(String errorMessage, int httpStatus, HttpServletResponse response) throws IOException {
        final Gson gson = new GsonBuilder().create();
        final String json = gson.toJson(new ErrorResponse(errorMessage));

        response.setHeader("Content-Type", "application/json");
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().write(json);
    }

}
