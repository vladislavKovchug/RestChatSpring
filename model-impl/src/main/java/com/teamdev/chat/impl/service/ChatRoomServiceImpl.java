package com.teamdev.chat.impl.service;

import com.teamdev.chat.dto.*;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.repository.ChatRoomRepository;
import com.teamdev.chat.repository.UserRepository;
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
    public Iterable<ChatRoomDTO> readAllChatRooms(UserId actor, TokenDTO token) {
        final List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<ChatRoomDTO> result = new ArrayList<>(chatRooms.size());
        for (ChatRoom chatRoom : chatRooms){
            result.add(new ChatRoomDTO(chatRoom.getId(), chatRoom.getName()));
        }

        return result;
    }

    @Override
    public ChatRoomDTO addChatRoom(String chatRoomName) {
        if(chatRoomRepository.findChatRoomByName(chatRoomName) != null){
            throw new RuntimeException("Error with create chat room. Chat room with name " + chatRoomName +
                    " already exists.");
        }
        final ChatRoom chatRoom = new ChatRoom(chatRoomName);
        chatRoomRepository.save(chatRoom);
        return new ChatRoomDTO(chatRoom.getId(), chatRoom.getName());
    }

    @Override
    public void deleteChatRoomByName(String chatRoomName) {
        final ChatRoom chatRoom = chatRoomRepository.findChatRoomByName(chatRoomName);
        if(chatRoom == null){
            throw new RuntimeException("Error delete not existed chat room with name " + chatRoomName);
        }
        chatRoomRepository.delete(chatRoom);
    }

    @Override
    public void deleteChatRoom(ChatRoomId chatRoomId) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if(chatRoom == null){
            throw new RuntimeException("Error delete not existed chat room with id " + Long.toString(chatRoomId.id));
        }
        chatRoomRepository.delete(chatRoom);
    }

    @Override
    public void joinChatRoom(UserId actor, ChatRoomId chatRoomId, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new RuntimeException("Error with join chat room. Chat room with id " + Long.toString(chatRoomId.id) +
                    " not found.");
        }

        final User user = userRepository.findOne(actor.id);
        if(chatRoom.getUsers().contains(user)){
            throw new RuntimeException("Error with join chat room. User is already in current chat room.");
        }

        user.addChatRoom(chatRoom);
        userRepository.save(user);
    }

    @Override
    public void leaveChatRoom(UserId actor, ChatRoomId chatRoomId, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new RuntimeException("Error with leave chat room. Chat room with id " + Long.toString(chatRoomId.id) +
                    " not found.");
        }

        final User user = userRepository.findOne(actor.id);
        if(!chatRoom.getUsers().contains(user)){
            throw new RuntimeException("Error with leave chat room. User is not in chat room.");
        }
        user.removeChatRoom(chatRoom);
        userRepository.save(user);
    }

    @Override
    public Iterable<UserProfileDTO> readChatRoomUserList(UserId actor, ChatRoomId chatRoomId, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new RuntimeException("Error with read chat room users. Chat room with id " + Long.toString(chatRoomId.id) +
                    " not found.");
        }

        final Set<User> chatRoomUsers = chatRoom.getUsers();
        final List<UserProfileDTO> userProfileDTOs = new ArrayList<>(chatRoomUsers.size());
        for(User user : chatRoomUsers){
            userProfileDTOs.add(new UserProfileDTO(user.getId(), user.getLogin(), user.getBirthday()));
        }
        return userProfileDTOs;
    }

}
