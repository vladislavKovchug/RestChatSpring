package com.teamdev.chat.request;

public class AddChatRoomRequest {
    private String name;

    public AddChatRoomRequest(String name) {
        this.name = name;
    }

    /*package*/ AddChatRoomRequest() {
    }

    public String getName() {
        return name;
    }
}
