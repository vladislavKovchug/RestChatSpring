package com.teamdev.database;

import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.entity.Message;
import com.teamdev.database.entity.Token;
import com.teamdev.database.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabase {


    private static List<User> users = new ArrayList<>();
    private static List<ChatRoom> chatRooms = new ArrayList<>();
    private static List<Message> messages = new ArrayList<>();
    private static List<Token> tokens = new ArrayList<>();

    private static long usersIndex = 0;
    private static long chatRoomsIndex = 0;
    private static long messagesIndex = 0;
    private static long tokensIndex = 0;

    public List<User> selectUsers() {
        return users;
    }

    public List<ChatRoom> selectChatRooms() {
        return chatRooms;
    }

    public List<Message> selectMessages() {
        return messages;
    }

    public List<Token> selectTokens() {
        return tokens;
    }

    public long getUsersIndex() {
        return usersIndex;
    }

    public long incrementUsersIndex() {
        return ++usersIndex;
    }

    public long getChatRoomsIndex() {
        return chatRoomsIndex;
    }

    public long incrementChatRoomsIndex() {
        return ++chatRoomsIndex;
    }

    public long getMessagesIndex() {
        return messagesIndex;
    }

    public long incrementMessagesIndex() {
        return ++messagesIndex;
    }

    public long getTokensIndex() {
        return tokensIndex;
    }

    public long incrementTokensIndex() {
        return ++tokensIndex;
    }

}
