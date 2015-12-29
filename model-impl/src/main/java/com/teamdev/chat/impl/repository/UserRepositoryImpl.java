package com.teamdev.chat.impl.repository;


import com.teamdev.chat.repository.UserRepository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserRepositoryImpl implements UserRepository {

    @Inject
    private ChatDatabase chatDatabase;

    @Override
    public User findOne(long id) {
        for (User entity : chatDatabase.selectUsers()) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return chatDatabase.selectUsers();
    }

    @Override
    public void save(User entity) {
        if (entity.getId() == -1) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementUsersIndex());
            chatDatabase.selectUsers().add(entity);
        } else {
            int index = 0;
            for (User user : chatDatabase.selectUsers()) {
                if (user.getId() == entity.getId()) {
                    chatDatabase.selectUsers().set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(User entity) {
        chatDatabase.selectUsers().remove(entity);
        entity.removeDependencies();
    }

    @Override
    public User findUserByName(String name) {
        final List<User> allUsers = findAll();
        for (User user : allUsers) {
            if (user.getLogin().equals(name)) {
                return user;
            }
        }

        return null;
    }
}
