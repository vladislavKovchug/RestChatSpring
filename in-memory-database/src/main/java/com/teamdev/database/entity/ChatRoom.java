package com.teamdev.database.entity;


import java.util.LinkedHashSet;
import java.util.Set;

public class ChatRoom {
    private long id;
    private String name;
    private Set<User> users = new LinkedHashSet<>();
    private Set<Message> messages = new LinkedHashSet<>();

    public ChatRoom() {
        this.id = -1;
    }

    public ChatRoom(String name) {
        this.id = -1;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        if (users.add(user)) {
            user.addChatRoom(this);
        }
    }

    public void removeUser(User user) {
        if (users.remove(user)) {
            user.removeChatRoom(this);
        }
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        if (messages.add(message)) {
            message.setChatRoom(this);
        }
    }

    public void removeMessage(Message message) {
        if (messages.remove(message)) {
            message.setChatRoom(null);
        }
    }

    public void removeDependencies() {
        for (Message message : new LinkedHashSet<>(messages)) {
            message.setUserTo(null);
        }
        for (User user : new LinkedHashSet<>(users)) {
            user.removeChatRoom(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom)) return false;

        ChatRoom chatRoom = (ChatRoom) o;

        if (id != chatRoom.id) return false;
        return !(name != null ? !name.equals(chatRoom.name) : chatRoom.name != null);

    }


    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
