package com.teamdev.chat.dto;

import java.util.Date;

public class MessageDTO {
    public final long id;
    public final long fromUserId;
    public final String fromUserName;
    public final long toUserId;
    public final String toUserName;
    public final String message;
    public final boolean privateMessage;
    private final Date date;

    public MessageDTO(long id, long fromUserId, String fromUserName, long toUserId, String toUserName, String message, boolean privateMessage, Date date) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.message = message;
        this.privateMessage = privateMessage;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}