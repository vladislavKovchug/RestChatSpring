package com.teamdev.chat.request;


public class PostMessageRequest {
    private String message;
    private String token;

    public PostMessageRequest(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public PostMessageRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
