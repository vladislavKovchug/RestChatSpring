package com.teamdev.chat.service;


import com.teamdev.chat.dto.MessageDTO;

public interface MessageService {

    Iterable<MessageDTO> readChatRoomMessages(long actor, long chatRoomId, long time, String token);
    void sendMessage(long actor, long chatRoomId, String messageText, String token);
    void sendPrivateMessage(long actor, long chatRoomId, String messageText, long receiverUserId, String token);

}
