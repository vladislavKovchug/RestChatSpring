package com.teamdev.chat.service;


import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;

public interface UserManagementService {

    UserProfileDTO register(RegisterUserDTO registerUserDTO);

    void deleteUser(UserId userId);

}
