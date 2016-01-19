package com.teamdev.chat.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;

    @ManyToOne
    private User user;
    private Date expireTime;

    /*package*/ Token() {
    }

    public Token(String token, User user, Date expireTime) {
        this.token = token;
        this.user = user;
        this.expireTime = expireTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
