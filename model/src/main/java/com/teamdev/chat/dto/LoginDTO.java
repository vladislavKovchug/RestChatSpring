package com.teamdev.chat.dto;

public class LoginDTO {
    public final long userId;
    public final String token;

    public LoginDTO(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
