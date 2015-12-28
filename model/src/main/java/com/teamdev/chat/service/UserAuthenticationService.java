package com.teamdev.chat.service;

public interface UserAuthenticationService {
    String login(String login, String password);

    void checkUserLogged(String token);

    long readCurrentUserId(String token);

    void logout(String token);

}
