package com.teamdev.chat.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String login;
    private String passwordHash;
    private Date birthday;

    public Cat() {
    }

    public Cat(String login, String passwordHash, Date birthday) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
