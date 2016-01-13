package com.teamdev.chat.impl.service;


import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.exception.UserException;
import com.teamdev.chat.service.UserManagementService;
import com.teamdev.chat.repository.UserRepository;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Inject
    private UserRepository userRepository;

    @Override
    public UserProfileDTO register(RegisterUserDTO registerUserDTO) {
        HashFunction hf = Hashing.sha256();
        String passwordHash = hf.newHasher().putString(registerUserDTO.password, Charsets.UTF_8).hash().toString();
        if (userRepository.findUserByName(registerUserDTO.login) != null) {
            throw new UserException("User " + registerUserDTO.login + " already exists.");
        }
        final User user = new User(registerUserDTO.login, passwordHash,
                registerUserDTO.getBirthday());
        userRepository.save(user);
        return  new UserProfileDTO(user.getId(), user.getLogin(), user.getBirthday());
    }

    @Override
    public void deleteUser(UserId userId) {
        final User user = userRepository.findOne(userId.id);
        if(user == null){
            throw new UserException("User with id " + Long.toString(userId.id) + " does not exists.");
        }
        userRepository.delete(user);
    }
}
