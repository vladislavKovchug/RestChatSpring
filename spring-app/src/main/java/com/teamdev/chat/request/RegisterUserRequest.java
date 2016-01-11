package com.teamdev.chat.request;


import java.util.Date;

public class RegisterUserRequest {
    private String login;
    private String password;
    private Date birthday;

    public RegisterUserRequest(String login, String password, Date birthday) {
        this.login = login;
        this.password = password;
        this.birthday = birthday;
    }

    public RegisterUserRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
