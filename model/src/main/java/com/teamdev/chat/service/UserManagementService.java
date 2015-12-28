package com.teamdev.chat.service;


import com.teamdev.chat.dto.RegisterUserDTO;

public interface UserManagementService {

    void register(RegisterUserDTO registerUserDTO);

    void deleteUser(long userId);

}
