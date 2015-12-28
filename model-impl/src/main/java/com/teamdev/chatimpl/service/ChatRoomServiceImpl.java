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

}
