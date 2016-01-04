package com.teamdev.chat.service;


import com.teamdev.chat.dto.ChatRoomId;
import com.teamdev.chat.dto.MessageDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;

public interface MessageService {

    Iterable<MessageDTO> readChatRoomMessages(UserId actor, ChatRoomId chatRoomId, long time, TokenDTO token);
    void sendMessage(UserId actor, ChatRoomId chatRoomId, String messageText, TokenDTO token);
    void sendPrivateMessage(UserId actor, ChatRoomId chatRoomId, String messageText, UserId receiverUserId, TokenDTO token);

}
