package com.teamdev.chat.request;


public class PostMessageRequest {
    private String message;
    private String token;

    public PostMessageRequest(String message, String token) {
        this.message = message;
        this.token = token;
    }

    /*package*/ PostMessageRequest() {
    }

    public String getMessage() {
        return message;
    }


    public String getToken() {
        return token;
    }

}
