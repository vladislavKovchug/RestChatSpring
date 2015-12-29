package com.teamdev.chat.dto;


import java.util.Date;

public class RegisterUserDTO {
    public final String login;
    public final String password;
    private final Date birthday;

    public RegisterUserDTO(String login, String password, Date birthday) {
        this.login = login;
        this.password = password;
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }
}