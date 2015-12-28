package com.teamdev.chatimpl.repository;




import java.util.List;

public interface Repository<Entity> {
    Entity findOne(long id);

    List<Entity> findAll();

    void save(Entity entity);

    void delete(Entity entity);
}
