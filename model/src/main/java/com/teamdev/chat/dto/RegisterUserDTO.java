package com.teamdev.chat.dto;


import java.util.Date;

public class RegisterUserDTO {
    public final String login;
    public final String password;
    public final long age;
    private final Date birthday;

    public RegisterUserDTO(String login, String password, long age, Date birthday) {
        this.login = login;
        this.password = password;
        this.age = age;
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }
}