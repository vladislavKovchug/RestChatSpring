package com.teamdev.chat.service;

import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;

public interface UserAuthenticationService {

    LoginDTO login(String login, String password);

    void validateToken(UserId userId, TokenDTO token);

    void logout(UserId actor, TokenDTO token);

}
