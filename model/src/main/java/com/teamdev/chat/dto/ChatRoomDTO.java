package com.teamdev.chat.dto;

public class ChatRoomDTO{
    public final long id;
    public final String name;

    public ChatRoomDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
}