package com.teamdev.chat.impl.service;


import com.teamdev.chat.dto.ChatRoomId;
import com.teamdev.chat.dto.MessageDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.repository.ChatRoomRepository;
import com.teamdev.chat.repository.MessageRepository;
import com.teamdev.chat.repository.UserRepository;
import com.teamdev.chat.service.MessageService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.entity.Message;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    public static final int FIVE_MINUTES = 300;
    @Inject
    private ChatRoomRepository chatRoomRepository;

    @Inject
    private UserAuthenticationService userAuthenticationService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Override
    public Iterable<MessageDTO> readChatRoomMessages(UserId actor, ChatRoomId chatRoomId, long time, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new RuntimeException("Error with getting chat room messages. Chat room with id " +
                    Long.toString(chatRoomId.id) + " not found.");
        }

        if (time == -1) { //if time not defined, should select messages for last 5 minutes
            time = new Date().getTime() - FIVE_MINUTES;
        }

        final List<Message> allUserMessagesAfter = messageRepository.findAllUserMessagesAfter(actor.id,
                chatRoomId.id, new Date(time));
        final ArrayList<MessageDTO> messageDTOs = new ArrayList<>(allUserMessagesAfter.size());
        for (Message message : allUserMessagesAfter) {

            if(message.getUserTo() == null){
                messageDTOs.add(new MessageDTO(message.getId(),
                        message.getUserFrom().getId(), message.getUserFrom().getLogin(),
                        (long)-1, "",
                        message.getMessage(), false, message.getDate()));
            } else {
                messageDTOs.add(new MessageDTO(message.getId(),
                        message.getUserFrom().getId(), message.getUserFrom().getLogin(),
                        message.getUserTo().getId(), message.getUserTo().getLogin(),
                        message.getMessage(), true, message.getDate()));
            }
        }

        return messageDTOs;
    }

    @Override
    public void sendMessage(UserId actor, ChatRoomId chatRoomId, String messageText, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new RuntimeException("No chat room with id " + Long.toString(chatRoomId.id) + " found.");
        }
        final User user = userRepository.findOne(actor.id);
        if(!chatRoom.getUsers().contains(user)){
            throw new RuntimeException("Error send message to not joined chat room.");
        }

        final Message message = new Message(user, new Date(), messageText);
        message.setChatRoom(chatRoom);
        messageRepository.save(message);
    }

    @Override
    public void sendPrivateMessage(UserId actor, ChatRoomId chatRoomId, String messageText, UserId receiverUserId, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new RuntimeException("No chat room with id " + Long.toString(chatRoomId.id) + " found.");
        }
        final User userFrom = userRepository.findOne(actor.id);
        if(!chatRoom.getUsers().contains(userFrom)){
            throw new RuntimeException("Error send message to not joined chat room.");
        }
        final User userTo = userRepository.findOne(receiverUserId.id);
        if(!chatRoom.getUsers().contains(userTo)){
            throw new RuntimeException("Error send message to user that not joined chat room.");
        }
        final Message message = new Message(userFrom, new Date(), messageText);
        message.setChatRoom(chatRoom);
        message.setUserTo(userTo);
        messageRepository.save(message);
    }

}
