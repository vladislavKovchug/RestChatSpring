package com.teamdev.chat.repository;




import com.teamdev.database.entity.DatabaseEntity;

import java.util.List;

public interface Repository<Entity extends DatabaseEntity> {
    Entity findOne(Long id);

    List<Entity> findAll();

    void save(Entity entity);

    void delete(Entity entity);

}
