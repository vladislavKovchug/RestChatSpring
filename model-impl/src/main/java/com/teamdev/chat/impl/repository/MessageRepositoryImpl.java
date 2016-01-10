package com.teamdev.chat.impl.repository;


import com.teamdev.chat.repository.MessageRepository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.entity.Message;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageRepositoryImpl implements MessageRepository {

    @Inject
    private ChatDatabase chatDatabase;

    @Override
    public Message findOne(long id) {
        for (Message entity : findAll()) {
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
        final List<Message> messages = findAll();
        if (entity.getId() == null) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementMessagesIndex());
            messages.add(entity);
        } else {
            int index = 0;
            for (Message message : messages) {
                if (message.getId() == entity.getId()) {
                    messages.set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(Message entity) {
        findAll().remove(entity);
        entity.removeDependencies();
    }

    @Override
    public List<Message> findAllUserMessagesAfter(long userId, long chatRoom, Date date) {
        final List<Message> allMessages = findAll();
        final ArrayList<Message> result = new ArrayList<>(allMessages.size());
        for (Message message : allMessages) {
            if (message.getDate().after(date)) {
                result.add(message);
            }
        }
        return result;
    }
}
