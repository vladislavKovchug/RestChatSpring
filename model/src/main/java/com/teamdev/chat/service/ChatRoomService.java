package com.teamdev.chat.service;

import com.teamdev.chat.dto.*;

public interface ChatRoomService {

    ChatRoomDTO addChatRoom(String chatRoomName);
    void deleteChatRoom(ChatRoomId chatRoomId);

    Iterable<ChatRoomDTO> readAllChatRooms(UserId actor, TokenDTO token);
    void joinChatRoom(UserId actor, ChatRoomId chatRoomId, TokenDTO token);
    void leaveChatRoom(UserId actor, ChatRoomId chatRoomId, TokenDTO token);
    Iterable<UserProfileDTO> readChatRoomUserList(UserId actor, ChatRoomId chatRoomId, TokenDTO token);
}
