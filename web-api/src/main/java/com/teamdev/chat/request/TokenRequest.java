package com.teamdev.chat.request;

public class TokenRequest {
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }

    /*package*/ TokenRequest() {
    }

    public String getToken() {
        return token;
    }

}
