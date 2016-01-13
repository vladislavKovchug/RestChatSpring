package com.teamdev.database;

import com.teamdev.database.entity.DatabaseEntity;
import com.teamdev.database.exception.DatabaseException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ChatDatabase {


    private static Map<Tables, Map<Long, DatabaseEntity>> database = new HashMap<>();
    private static Map<Tables, AtomicLong> databaseIndex = new HashMap<>();

    public Map<Long, DatabaseEntity> selectTable(Tables table) {
        if (database.containsKey(table)) {
            return database.get(table);
        }
        throw new DatabaseException("Error on SELECT. No table with name=" + table + ".");
    }

    public void insertIntoTable(Tables table, DatabaseEntity line) {

        if (database.containsKey(table) && databaseIndex.containsKey(table)) {
            Long index = databaseIndex.get(table).incrementAndGet();
            line.setId(index);
            database.get(table).put(index, line);
        }
    }

    public void updateInTable(Tables table, DatabaseEntity line, Long id) {
        int index = 0;
        if (!database.containsKey(table)) {
            throw new DatabaseException("Error on UPDATE. No table with name=" + table + " was found.");
        }

        if (database.get(table).get(id) == null) {
            throw new DatabaseException("Error on UPDATE. No entity with id=" + id + " was found.");
        }

        database.get(table).put(id, line);
    }

    public void deleteFromTable(Tables table, Long id) {
        if (!database.containsKey(table)) {
            throw new DatabaseException("Error on DELETE. No table with name=" + table + " was found.");
        }

        if (database.get(table).get(id) == null) {
            throw new DatabaseException("Error on DELETE. No entity with id=" + id + " was found.");
        }

        database.get(table).remove(id);
    }

    public void createTable(Tables table) {
        if (database.containsKey(table)) {
            throw new DatabaseException("Table " + table.name() + "already exists");
        }
        database.put(table, new HashMap<>());
        databaseIndex.put(table, new AtomicLong());
    }

}
