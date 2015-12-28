package com.teamdev.chat.dto;

public class ChatRoomDTO{
    public final Long id;
    public final String name;

    public ChatRoomDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}