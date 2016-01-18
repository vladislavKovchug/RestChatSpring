package com.teamdev.chat.impl.service;


import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.entity.User;
import com.teamdev.chat.exception.UserException;
import com.teamdev.chat.hrepository.UserRepository;
import com.teamdev.chat.service.UserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository userRepository;

    @Override
    public UserProfileDTO readUserProfile(UserId actor, UserId userId, TokenDTO token) {
        final User user = userRepository.findOne(userId.id);
        if (user == null) {
            throw new UserException("No user found with id " + Long.toString(userId.id) + ".");
        }

        return new UserProfileDTO(user.getId(), user.getLogin(), user.getBirthday());
    }

    @Override
    public Iterable<UserProfileDTO> readAllUsersProfiles(UserId actor, TokenDTO token) {
        final Iterable<User> allUsers = userRepository.findAll();
        final ArrayList<UserProfileDTO> userProfileDTOs = new ArrayList<>();
        for (User user : allUsers) {
            userProfileDTOs.add(new UserProfileDTO(user.getId(), user.getLogin(), user.getBirthday()));
        }
        return userProfileDTOs;
    }

}
