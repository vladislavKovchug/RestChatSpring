package com.teamdev.chat.impl.repository;


import com.teamdev.chat.repository.ChatRoomRepository;
import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.ChatDatabase;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    @Inject
    private ChatDatabase chatDatabase;

    @Override
    public ChatRoom findOne(long id) {
        for (ChatRoom entity : findAll()) {
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
        final List<ChatRoom> chatRooms = findAll();
        if (entity.getId() == null) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementChatRoomsIndex());
            chatRooms.add(entity);
        } else {
            int index = 0;
            for (ChatRoom chatRoom : chatRooms) {
                if (chatRoom.getId() == entity.getId()) {
                    chatRooms.set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(ChatRoom entity) {
        findAll().remove(entity);
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
