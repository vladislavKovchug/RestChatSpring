package com.teamdev.chat.service;


import com.teamdev.chat.dto.UserProfileDTO;

public interface UserService {

    UserProfileDTO readUserProfile(long actor, long userId, String token);

    UserProfileDTO readCurrentUserProfile(long actor, String token);

    Iterable<UserProfileDTO> readAllUsersProfile(long actor, String token);

}
