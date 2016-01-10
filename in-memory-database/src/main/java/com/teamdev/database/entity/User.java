package com.teamdev.database.entity;


import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class User {
    private Long id;
    private String login;
    private String passwordHash;
    private Date birthday;
    private Set<ChatRoom> chatRooms = new LinkedHashSet<>();
    private Set<Message> messages = new LinkedHashSet<>();

    public User() {

    }

    public User(String login, String passwordHash, Date birthday) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void addChatRoom(ChatRoom chatRoom) {
        if (chatRooms.add(chatRoom)) {
            chatRoom.addUser(this);
        }

    }

    public void removeChatRoom(ChatRoom chatRoom) {
        if (chatRooms.remove(chatRoom)) {
            chatRoom.removeUser(this);
        }
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        if (messages.add(message)) {
            message.setUserTo(this);
        }
    }

    public void removeMessage(Message message) {
        if (messages.remove(message)) {
            message.setUserTo(null);
        }
    }

    public void removeDependencies() {
        for (Message message : new LinkedHashSet<>(messages)) {
            message.setUserTo(null);
        }
        for (ChatRoom chatRoom : new LinkedHashSet<>(chatRooms)) {
            chatRoom.removeUser(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (passwordHash != null ? !passwordHash.equals(user.passwordHash) : user.passwordHash != null) return false;
        return !(birthday != null ? !birthday.equals(user.birthday) : user.birthday != null);

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
