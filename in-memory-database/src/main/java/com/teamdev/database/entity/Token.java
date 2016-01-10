package com.teamdev.database.entity;


import java.util.Date;

public class Token implements DatabaseEntity {
    private Long id;
    private String token;
    private long userId;
    private Date expireTime;

    public Token() {

    }

    public Token(String token, long userId, Date expireTime) {
        this.token = token;
        this.userId = userId;
        this.expireTime = expireTime;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
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
    public void removeDependencies() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;

        Token token1 = (Token) o;

        if (userId != token1.userId) return false;
        if (id != null ? !id.equals(token1.id) : token1.id != null) return false;
        if (token != null ? !token.equals(token1.token) : token1.token != null) return false;
        return !(expireTime != null ? !expireTime.equals(token1.expireTime) : token1.expireTime != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
