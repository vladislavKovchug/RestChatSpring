package com.teamdev.chat.service;

import com.teamdev.chat.dto.*;

public interface ChatRoomService {

    ChatRoomDTO addChatRoom(String chatRoomName);
    void deleteChatRoomByName(String chatRoomName);
    void deleteChatRoom(long chatRoomId);

    Iterable<ChatRoomDTO> readAllChatRooms(long actor, String token);
    void joinChatRoom(long actor, long chatRoomId, String token);
    void leaveChatRoom(long actor, long chatRoomId, String token);
    Iterable<UserProfileDTO> readChatRoomUserList(long actor, long chatRoomId, String token);
}
