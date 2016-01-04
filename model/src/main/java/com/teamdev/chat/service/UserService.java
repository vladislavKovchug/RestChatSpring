package com.teamdev.chat.service;


import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;

public interface UserService {

    UserProfileDTO readUserProfile(UserId actor, UserId userId, TokenDTO token);

    UserProfileDTO readCurrentUserProfile(UserId actor, TokenDTO token);

    Iterable<UserProfileDTO> readAllUsersProfile(UserId actor, TokenDTO token);

}
