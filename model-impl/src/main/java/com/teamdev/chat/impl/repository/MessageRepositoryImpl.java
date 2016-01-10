package com.teamdev.chat.impl.repository;


import com.teamdev.chat.repository.MessageRepository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.Tables;
import com.teamdev.database.entity.Message;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageRepositoryImpl extends AbstractRepository<Message> implements MessageRepository {

    @Override
    protected Tables getTable() {
        return Tables.MESSAGES_TABLE;
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
