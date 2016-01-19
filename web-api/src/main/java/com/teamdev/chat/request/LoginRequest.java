package com.teamdev.chat.request;

public class LoginRequest {
    private String login;
    private String password;

    /*package*/ LoginRequest() {
    }

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
