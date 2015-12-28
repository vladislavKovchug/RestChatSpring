package com.teamdev.database.entity;


import java.util.Date;

public class Token {
    private long id;
    private String token;
    private long userId;
    private Date expireTime;

    public Token() {
        this.id = -1;
    }

    public Token(String token, long userId, Date expireTime) {
        this.id = -1;
        this.token = token;
        this.userId = userId;
        this.expireTime = expireTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        if (userId != token1.userId) return false;
        if (!token.equals(token1.token)) return false;
        return expireTime.equals(token1.expireTime);

    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + expireTime.hashCode();
        return result;
    }
}
