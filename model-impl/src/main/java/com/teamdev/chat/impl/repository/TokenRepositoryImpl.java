package com.teamdev.chat.impl.repository;

import com.teamdev.chat.repository.TokenRepository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.entity.Token;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class TokenRepositoryImpl implements TokenRepository {

    @Inject
    private ChatDatabase chatDatabase;

    @Override
    public Token findByToken(String token) {
        if(token == null){
            return null;
        }
        for (Token entity : findAll()) {
            if (token.equals(entity.getToken())) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public Token findOne(long id) {
        for (Token entity : findAll()) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<Token> findAll() {
        return chatDatabase.selectTokens();
    }

    @Override
    public void save(Token entity) {
        final List<Token> tokens = findAll();
        if (entity.getId() == null) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementTokensIndex());
            tokens.add(entity);
        } else {
            int index = 0;
            for (Token token : tokens) {
                if (token.getId() == entity.getId()) {
                    tokens.set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(Token entity) {
        findAll().remove(entity);
    }
}
