package com.teamdev.chat.service;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;

public interface UserService {

    UserProfileDTO readUserProfile(UserId actor, UserId userId, TokenDTO token);

    Iterable<UserProfileDTO> readAllUsersProfiles(UserId actor, TokenDTO token);

    Iterable<ChatRoomDTO> readUserJoinedChats(UserId actor, TokenDTO token);

}
