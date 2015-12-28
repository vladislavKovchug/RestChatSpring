package com.teamdev.chatimpl.service;

import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chatimpl.repository.ChatRoomRepository;
import com.teamdev.chatimpl.repository.UserRepository;
import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Inject
    private ChatRoomRepository chatRoomRepository;

    @Inject
    private UserAuthenticationService userAuthenticationService;

    @Inject
    private UserRepository userRepository;

    @Override
    public Iterable<ChatRoomDTO> readAllChatRooms(String token) {
        userAuthenticationService.checkUserLogged(token);

        final List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<ChatRoomDTO> result = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms){
            result.add(new ChatRoomDTO(chatRoom.getId(), chatRoom.getName()));
        }

        return result;
    }

    @Override
    public void addChatRoom(String chatRoomName) {
        if(chatRoomRepository.findChatRoomByName(chatRoomName) != null){
            throw new RuntimeException("Error with create chat room. Chat room with name " + chatRoomName +
                    " already exists.");
        }
        chatRoomRepository.save(new ChatRoom(chatRoomName));
    }

    @Override
    public void joinChatRoom(String token, long chatRoomId) {
        final long userId = userAuthenticationService.readCurrentUserId(token);
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId);
        if (chatRoom == null) {
            throw new RuntimeException("Error with join chat room. Chat room with id " + Long.toString(chatRoomId) +
                    " not found.");
        }

        final User user = userRepository.findOne(userId);
        if(chatRoom.getUsers().contains(user)){
            throw new RuntimeException("Error with join chat room. User is already in current chat room.");
        }

        user.addChatRoom(chatRoom);
        userRepository.save(user);
    }

    @Override
    public void leaveChatRoom(String token, long chatRoomId) {
        final long userId =userAuthenticationService.readCurrentUserId(token);
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId);
        if (chatRoom == null) {
            throw new RuntimeException("Error with leave chat room. Chat room with id " + Long.toString(chatRoomId) +
                    " not found.");
        }

        final User user = userRepository.findOne(userId);
        if(!chatRoom.getUsers().contains(user)){
            throw new RuntimeException("Error with leave chat room. User is not in chat room.");
        }
        user.removeChatRoom(chatRoom);
        userRepository.save(user);
    }

    @Override
    public Iterable<UserProfileDTO> readChatRoomUsersList(String token, long chatRoomId) {
        final long userId = userAuthenticationService.readCurrentUserId(token);
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId);
        if (chatRoom == null) {
            throw new RuntimeException("Error with read chat room users. Chat room with id " + Long.toString(chatRoomId) +
                    " not found.");
        }

        final Set<User> chatRoomUsers = chatRoom.getUsers();
        final ArrayList<UserProfileDTO> userProfileDTOs = new ArrayList<>();
        for(User user : chatRoomUsers){
            userProfileDTOs.add(new UserProfileDTO(user.getId(), user.getLogin(), user.getAge(), user.getBirthday()));
        }
        return userProfileDTOs;
    }

}
