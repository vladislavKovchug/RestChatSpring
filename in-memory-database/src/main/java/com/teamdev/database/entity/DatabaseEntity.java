package com.teamdev.database.entity;

public interface DatabaseEntity {
    Long getId();

    void setId(Long id);

    void removeDependencies();
}
