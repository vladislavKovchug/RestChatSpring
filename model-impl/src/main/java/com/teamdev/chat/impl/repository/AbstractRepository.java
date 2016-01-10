package com.teamdev.chat.impl.repository;

import com.teamdev.chat.repository.Repository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.Tables;
import com.teamdev.database.entity.DatabaseEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository<Entity extends DatabaseEntity> implements Repository<Entity> {

    @Inject
    ChatDatabase chatDatabase;

    @Override
    public Entity findOne(Long id) {
        final Map<Long, DatabaseEntity> table = chatDatabase.selectTable(getTable());

        return (Entity)table.get(id);
    }

    @Override
    public List<Entity> findAll() {
        final Map<Long, DatabaseEntity> table = chatDatabase.selectTable(getTable());
        final Collection<DatabaseEntity> values = table.values();
        List<Entity> result = new ArrayList<>();
        for(DatabaseEntity entity : values){
            result.add((Entity)entity);
        }
        return result;
    }

    @Override
    public void save(Entity entity) {
        if (entity.getId() == null) { //if id not defined insert, else update
            chatDatabase.insertIntoTable(getTable(), entity);
        } else {
            chatDatabase.updateInTable(getTable(), entity, entity.getId());
        }
    }

    @Override
    public void delete(Entity entity) {
        chatDatabase.deleteFromTable(getTable(), entity.getId());
        entity.removeDependencies();
    }

    protected abstract Tables getTable();

}
