package com.teamdev.chatimpl.service;


import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserService;
import com.teamdev.chatimpl.repository.UserRepository;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Inject
    private UserAuthenticationService userAuthenticationService;

    @Inject
    private UserRepository userRepository;

    @Override
    public UserProfileDTO readUserProfile(String token, long userId) {
        userAuthenticationService.readCurrentUserId(token);
        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new RuntimeException("No user found with id " + Long.toString(userId) + ".");
        }

        return new UserProfileDTO(user.getId(), user.getLogin(), user.getAge(), user.getBirthday());
    }

    @Override
    public UserProfileDTO readCurrentUserProfile(String token) {
        final long userId = userAuthenticationService.readCurrentUserId(token);
        final User user = userRepository.findOne(userId);
        return new UserProfileDTO(user.getId(), user.getLogin(), user.getAge(), user.getBirthday());
    }

    @Override
    public Iterable<UserProfileDTO> readAllUsersProfile(String token) {
        userAuthenticationService.readCurrentUserId(token);
        final List<User> allUsers = userRepository.findAll();
        final ArrayList<UserProfileDTO> userProfileDTOs = new ArrayList<>();
        for (User user : allUsers) {
            userProfileDTOs.add(new UserProfileDTO(user.getId(), user.getLogin(), user.getAge(), user.getBirthday()));
        }
        return userProfileDTOs;
    }
}
