package com.teamdev.chat.repository;


import com.teamdev.database.entity.User;

public interface UserRepository extends Repository<User> {
    User findUserByName(String name);
}
