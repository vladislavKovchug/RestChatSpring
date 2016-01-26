package com.teamdev.chat.service;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.entity.ChatRoom;
import com.teamdev.chat.entity.User;
import com.teamdev.chat.exception.UserException;
import com.teamdev.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
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

    @Override
    public Iterable<ChatRoomDTO> readUserJoinedChats(UserId actor, TokenDTO token) {
        final User user = userRepository.findOne(actor.id);
        if (user == null) {
            throw new UserException("No user found with id " + Long.toString(actor.id) + ".");
        }
        final Set<ChatRoom> chatRooms = user.getChatRooms();
        List<ChatRoomDTO> result = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms){
            result.add(new ChatRoomDTO(chatRoom.getId(), chatRoom.getName()));
        }

        return result;
    }
}
