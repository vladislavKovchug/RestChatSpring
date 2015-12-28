package com.teamdev.chat.service;

import com.teamdev.chat.dto.*;

public interface ChatRoomService {

    Iterable<ChatRoomDTO> readAllChatRooms(String token);
    void addChatRoom(String chatRoomName);
}
