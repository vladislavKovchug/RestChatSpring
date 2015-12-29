package com.teamdev.chat.service;

import com.teamdev.chat.dto.TokenDTO;

public interface UserAuthenticationService {

    TokenDTO login(String login, String password);

    void validateToken(long userId, String token);

    void logout(long actor, String token);

}
