package com.teamdev.chat.impl.repository;


import com.teamdev.chat.repository.UserRepository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.Tables;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserRepositoryImpl extends AbstractRepository<User> implements UserRepository {

    @Override
    protected Tables getTable() {
        return Tables.USERS_TABLE;
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
