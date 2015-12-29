package com.teamdev.chat.dto;

public class TokenDTO {
    public final long userId;
    public final String token;

    public TokenDTO(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
