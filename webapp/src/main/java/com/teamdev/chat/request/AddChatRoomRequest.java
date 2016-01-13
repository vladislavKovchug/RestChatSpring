package com.teamdev.chat.request;

public class AddChatRoomRequest {
    private String name;

    public AddChatRoomRequest(String name) {
        this.name = name;
    }

    public AddChatRoomRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
