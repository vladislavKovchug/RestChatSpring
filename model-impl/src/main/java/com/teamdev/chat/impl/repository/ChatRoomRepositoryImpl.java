package com.teamdev.chat.impl.repository;


import com.teamdev.chat.repository.ChatRoomRepository;
import com.teamdev.database.Tables;
import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service
public class ChatRoomRepositoryImpl extends AbstractRepository<ChatRoom> implements ChatRoomRepository {

    @Override
    protected Tables getTable() {
        return Tables.CHAT_ROOMS_TABLE;
    }

    @Override
    public ChatRoom findChatRoomByName(String name) {
        final List<ChatRoom> allChatRooms = findAll();
        for (ChatRoom chatRoom : allChatRooms){
            if(chatRoom.getName().equals(name)){
                return chatRoom;
            }
        }

        return null;
    }

}
