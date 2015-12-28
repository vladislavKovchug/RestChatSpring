package com.teamdev.chatimpl.repository;


import com.teamdev.database.entity.Message;

import java.util.Date;
import java.util.List;

public interface MessageRepository extends Repository<Message> {
    List<Message> findAllUserMessagesAfter(long userId, long chatRoom, Date date);
}
