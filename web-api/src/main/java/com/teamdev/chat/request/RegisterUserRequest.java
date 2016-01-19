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

    /*package*/ RegisterUserRequest() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public long getBirthday() {
        return birthday;
    }

}
