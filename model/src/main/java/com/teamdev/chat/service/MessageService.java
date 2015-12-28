package com.teamdev.chat.service;


import com.teamdev.chat.dto.MessageDTO;

public interface MessageService {

    Iterable<MessageDTO> readChatRoomMessages(String token, long chatRoomId, long time);
    void sendMessage(String token, long chatRoomId, String messageText);
    void sendPrivateMessage(String token, long chatRoomId, String messageText, long receiverUserId);

}
