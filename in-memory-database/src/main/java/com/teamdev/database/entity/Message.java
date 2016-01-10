package com.teamdev.database.entity;


import java.util.Date;

public class Message {
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

    public Long getId() {
        return id;
    }

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
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (id != message1.id) return false;
        if (date != null ? !date.equals(message1.date) : message1.date != null) return false;
        return !(message != null ? !message.equals(message1.message) : message1.message != null);

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
