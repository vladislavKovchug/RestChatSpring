package com.teamdev.chat.request;


public class RegisterUserRequest {
    private String login;
    private String password;
    private long birthday;

    public RegisterUserRequest(String login, String password, long birthday) {
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

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
}
