package com.teamdev.chat.request;

public class TokenRequest {
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }

    public TokenRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
