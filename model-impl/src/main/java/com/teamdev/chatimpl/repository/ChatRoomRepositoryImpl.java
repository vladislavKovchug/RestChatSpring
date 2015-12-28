package com.teamdev.chatimpl.repository;


import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.ChatDatabase;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    @Inject
    ChatDatabase chatDatabase;

    @Override
    public ChatRoom findOne(long id) {
        for (ChatRoom entity : chatDatabase.selectChatRooms()) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<ChatRoom> findAll() {
        return chatDatabase.selectChatRooms();
    }

    @Override
    public void save(ChatRoom entity) {
        if (entity.getId() == -1) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementChatRoomsIndex());
            chatDatabase.selectChatRooms().add(entity);
        } else {
            int index = 0;
            for (ChatRoom chatRoom : chatDatabase.selectChatRooms()) {
                if (chatRoom.getId() == entity.getId()) {
                    chatDatabase.selectChatRooms().set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(ChatRoom entity) {
        chatDatabase.selectChatRooms().remove(entity);
        entity.removeDependencies();
    }

    @Override
    public ChatRoom findChatRoomByName(String name) {
        final List<ChatRoom> allChatRooms = findAll();
        for (ChatRoom chatRoom : allChatRooms) {
            if (chatRoom.getName().equals(name)) {
                return chatRoom;
            }
        }

        return null;
    }
}
