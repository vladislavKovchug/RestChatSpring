package com.teamdev.chat.service;

public interface UserAuthenticationService {
    String login(String login, String password);

    long checkUserLogged(String token);
}
