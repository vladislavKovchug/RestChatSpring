package com.teamdev.database.entity;


import java.util.LinkedHashSet;
import java.util.Set;

public class ChatRoom implements DatabaseEntity {
    private Long id;
    private String name;
    private Set<User> users = new LinkedHashSet<>();
    private Set<Message> messages = new LinkedHashSet<>();

    public ChatRoom() {

    }

    public ChatRoom(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
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

    @Override
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

        if (id != null ? !id.equals(chatRoom.id) : chatRoom.id != null) return false;
        return !(name != null ? !name.equals(chatRoom.name) : chatRoom.name != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
