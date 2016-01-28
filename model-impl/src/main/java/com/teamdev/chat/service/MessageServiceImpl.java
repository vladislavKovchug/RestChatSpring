package com.teamdev.chat.service;


import com.teamdev.chat.dto.ChatRoomId;
import com.teamdev.chat.dto.MessageDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.entity.ChatRoom;
import com.teamdev.chat.entity.Message;
import com.teamdev.chat.entity.User;
import com.teamdev.chat.exception.ChatException;
import com.teamdev.chat.repository.ChatRoomRepository;
import com.teamdev.chat.repository.MessageRepository;
import com.teamdev.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    public static final int FIVE_MINUTES = 300*1000;
    @Inject
    private ChatRoomRepository chatRoomRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Override
    public Iterable<MessageDTO> readChatRoomMessages(UserId actor, ChatRoomId chatRoomId, long time, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new ChatException("Error with getting chat room messages. Chat room with id " +
                    Long.toString(chatRoomId.id) + " not found.");
        }

        final User user = userRepository.findOne(actor.id);
        if(!chatRoom.getUsers().contains(user)){
            throw new ChatException("Error with getting chat room messages. User not joined to chat " +
                    chatRoom.getName() + " not found.");
        }



        if (time == -1) { //if time not defined, should select messages for last 5 minutes
            time = new Date().getTime() - FIVE_MINUTES;
        }

        final List<Message> allUserMessagesAfter = messageRepository.findAllUserMessagesAfter(actor.id,
                chatRoomId.id, new Date(time+300));
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
    public MessageDTO sendMessage(UserId actor, ChatRoomId chatRoomId, String messageText, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new ChatException("No chat room with id " + Long.toString(chatRoomId.id) + " found.");
        }
        final User user = userRepository.findOne(actor.id);
        if(!chatRoom.getUsers().contains(user)){
            throw new ChatException("Error send message to not joined chat room.");
        }

        final Message message = new Message(user, null, chatRoom, new Date(), messageText);

        messageRepository.save(message);
        final Long id = message.getUserFrom() == null ? -1 : message.getUserFrom().getId();
        final String login = message.getUserFrom() == null ? "deleted user" : message.getUserFrom().getLogin();
        return new MessageDTO(message.getId(),
                id, login,
                (long)-1, "",
                message.getMessage(), false, message.getDate());
    }

    @Override
    public MessageDTO sendPrivateMessage(UserId actor, ChatRoomId chatRoomId, String messageText, UserId receiverUserId, TokenDTO token) {
        final ChatRoom chatRoom = chatRoomRepository.findOne(chatRoomId.id);
        if (chatRoom == null) {
            throw new ChatException("No chat room with id " + Long.toString(chatRoomId.id) + " found.");
        }
        final User userFrom = userRepository.findOne(actor.id);
        if(!chatRoom.getUsers().contains(userFrom)){
            throw new ChatException("Error send message to not joined chat room.");
        }
        final User userTo = userRepository.findOne(receiverUserId.id);
        if(!chatRoom.getUsers().contains(userTo)){
            throw new ChatException("Error send message to user that not joined chat room.");
        }
        final Message message = new Message(userFrom, userTo, chatRoom, new Date(), messageText);
        messageRepository.save(message);
        final Long idFrom = message.getUserFrom() == null ? -1 : message.getUserFrom().getId();
        final String loginFrom = message.getUserFrom() == null ? "deleted user" : message.getUserFrom().getLogin();
        final Long idTo = message.getUserTo() == null ? -1 : message.getUserTo().getId();
        final String loginTo = message.getUserTo() == null ? "deleted user" : message.getUserTo().getLogin();

        return new MessageDTO(message.getId(),
                idFrom, loginFrom,
                idTo, loginTo,
                message.getMessage(), true, message.getDate());
    }

}
