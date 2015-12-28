package com.teamdev.chatimpl.repository;

import com.teamdev.database.entity.ChatRoom;

public interface ChatRoomRepository extends Repository<ChatRoom> {
    ChatRoom findChatRoomByName(String name);
}
