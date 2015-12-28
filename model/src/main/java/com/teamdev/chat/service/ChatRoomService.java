package com.teamdev.chat.service;

import com.teamdev.chat.dto.*;

public interface ChatRoomService {

    Iterable<ChatRoomDTO> readAllChatRooms(String token);
    void addChatRoom(String chatRoomName);
    void joinChatRoom(String token, long chatRoomId);
    void leaveChatRoom(String token, long chatRoomId);
    Iterable<UserProfileDTO> readChatRoomUsersList(String token, long chatRoomId);
}
