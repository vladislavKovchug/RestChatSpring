package com.teamdev.chatimpl.repository;


import com.teamdev.database.ChatDatabase;
import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.entity.Message;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageRepositoryImpl implements MessageRepository {

    @Inject
    ChatDatabase chatDatabase;

    @Override
    public Message findOne(long id) {
        for (Message entity : chatDatabase.selectMessages()) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return chatDatabase.selectMessages();
    }

    @Override
    public void save(Message entity) {
        if (entity.getId() == -1) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementMessagesIndex());
            chatDatabase.selectMessages().add(entity);
        } else {
            int index = 0;
            for (Message message : chatDatabase.selectMessages()) {
                if (message.getId() == entity.getId()) {
                    chatDatabase.selectMessages().set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(Message entity) {
        chatDatabase.selectMessages().remove(entity);
        entity.removeDependencies();
    }

    @Override
    public List<Message> findAllUserMessagesAfter(long userId, long chatRoom, Date date) {
        final List<Message> allMessages = findAll();
        final ArrayList<Message> result = new ArrayList<>();
        for (Message message : allMessages) {
            if (message.getDate().after(date)) {
                result.add(message);
            }
        }
        return result;
    }
}
