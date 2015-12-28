package com.teamdev.database;

import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.entity.Message;
import com.teamdev.database.entity.User;

import java.util.*;

public class ChatDatabase {


    private static List<User> users = new ArrayList<>();
    private static List<ChatRoom> chatRooms = new ArrayList<>();
    private static List<Message> messages = new ArrayList<>();

    private static long usersIndex = 0;
    private static long chatRoomsIndex = 0;
    private static long messagesIndex = 0;

    public List<User> selectUsers() {
        return users;
    }

    public List<ChatRoom> selectChatRooms() {
        return chatRooms;
    }

    public List<Message> selectMessages() {
        return messages;
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

    /*private static Map<Tables, List<DatabaseEntity>> database = new HashMap<Tables, List<DatabaseEntity>>();

    public List<DatabaseEntity> selectTable(Tables table) {
        if(database.containsKey(table)){
            return database.get(table);
        }
        return new ArrayList<DatabaseEntity>();
    }

    public void insertIntoTable(Tables table, DatabaseEntity line){
        if(database.containsKey(table)){
            line.setId(database.get(table).size() + 1);
            database.get(table).add(line);
        }
    }

    public void updateInTable(Tables table, DatabaseEntity line, long id){
        int index = 0;
        if(!database.containsKey(table)){
            throw new RuntimeException("Error on UPDATE. No entity with id=" + Long.toString(id) + " was found.");
        }

        for(DatabaseEntity entity: database.get(table)){
            if(entity.getId() == id){
                database.get(table).set(index, line);
                break;
            }
            index++;
        }

    }

    public void deleteFromTable(Tables table, long id){
        if(!database.containsKey(table)){
            throw new RuntimeException("Error on DELETE. No entity with id=" + Long.toString(id) + " was found.");
        }

        for(DatabaseEntity entity: database.get(table)){
            if(entity.getId() == id){
                database.get(table).remove(entity);
                break;
            }
        }
    }

    public void createTable(Tables table) {
        if(database.containsKey(table)){
            throw new RuntimeException("Table " + table.name() + "already exists");
        }
        database.put(table, new ArrayList<DatabaseEntity>());
    }*/

    /*ChatDatabase() {
        createTable(Tables.USERS_TABLE);
        createTable(Tables.CHAT_ROOMS_TABLE);
        createTable(Tables.MESSAGES_TABLE);

        HashFunction hf = Hashing.sha256();

        insertIntoTable(Tables.USERS_TABLE, new User("user1",
                hf.newHasher().putString("12345", Charsets.UTF_8).hash().toString(), 20, new Date()));
        insertIntoTable(Tables.USERS_TABLE, new User("user2",
                hf.newHasher().putString("big_password123", Charsets.UTF_8).hash().toString(), 20, new Date()));

        insertIntoTable(Tables.CHAT_ROOMS_TABLE, new ChatRoom("chat"));
    }*/
}
