package com.teamdev.chatimpl.repository;

import com.teamdev.database.ChatDatabase;
import com.teamdev.database.entity.Token;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class TokenRepositoryImpl implements TokenRepository {

    @Inject
    ChatDatabase chatDatabase;

    @Override
    public Token findByToken(String token) {
        if(token == null){
            return null;
        }
        for (Token entity : chatDatabase.selectTokens()) {
            if (token.equals(entity.getToken())) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public Token findOne(long id) {
        for (Token entity : chatDatabase.selectTokens()) {
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
        if (entity.getId() == -1) { //if id not defined insert, else update
            entity.setId(chatDatabase.incrementTokensIndex());
            chatDatabase.selectTokens().add(entity);
        } else {
            int index = 0;
            for (Token token : chatDatabase.selectTokens()) {
                if (token.getId() == entity.getId()) {
                    chatDatabase.selectTokens().set(index, entity);
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void delete(Token entity) {
        chatDatabase.selectTokens().remove(entity);
    }
}
