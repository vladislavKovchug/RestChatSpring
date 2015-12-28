package com.teamdev.chatimpl.service;


import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.service.UserManagementService;
import com.teamdev.chatimpl.repository.UserRepository;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Inject
    private UserRepository userRepository;

    @Override
    public void register(RegisterUserDTO registerUserDTO) {
        HashFunction hf = Hashing.sha256();
        String passwordHash = hf.newHasher().putString(registerUserDTO.password, Charsets.UTF_8).hash().toString();
        if (userRepository.findUserByName(registerUserDTO.login) != null) {
            throw new RuntimeException("User " + registerUserDTO.login + " already exists.");
        }
        userRepository.save(new User(registerUserDTO.login, passwordHash, registerUserDTO.age,
                registerUserDTO.getBirthday()));
    }

    @Override
    public void deleteUser(long userId) {
        final User user = userRepository.findOne(userId);
        if(user == null){
            throw new RuntimeException("User with id " + Long.toString(userId) + " does not exists.");
        }
        userRepository.delete(user);
    }
}
