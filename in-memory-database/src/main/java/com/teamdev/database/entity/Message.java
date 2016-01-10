package com.teamdev.database.entity;


import java.util.Date;

public class Message implements DatabaseEntity {
    private Long id;
    private User userFrom;
    private User userTo;
    private ChatRoom chatRoom;
    private Date date;
    private String message;

    public Message() {

    }

    public Message(User userFrom, Date date, String message) {
        this.userFrom = userFrom;
        this.date = date;
        this.message = message;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        if (this.userTo != null && !this.userTo.equals(userTo)) { // if user changed
            this.userTo.removeMessage(this);
        }
        this.userTo = userTo;
        if (userTo != null) {
            this.userTo.addMessage(this);
        }
    }

    @Override
    public void removeDependencies() {
        if (chatRoom != null) {
            chatRoom.removeMessage(this);
        }
        if (userTo != null) {
            userTo.removeMessage(this);
        }
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != null && !this.chatRoom.equals(chatRoom)) { //if chatroom changed
            this.chatRoom.removeMessage(this);
        }
        this.chatRoom = chatRoom;
        if (chatRoom != null) {
            this.chatRoom.addMessage(this);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message1 = (Message) o;

        if (id != null ? !id.equals(message1.id) : message1.id != null) return false;
        if (userFrom != null ? !userFrom.equals(message1.userFrom) : message1.userFrom != null) return false;
        if (userTo != null ? !userTo.equals(message1.userTo) : message1.userTo != null) return false;
        if (chatRoom != null ? !chatRoom.equals(message1.chatRoom) : message1.chatRoom != null) return false;
        if (date != null ? !date.equals(message1.date) : message1.date != null) return false;
        return !(message != null ? !message.equals(message1.message) : message1.message != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
