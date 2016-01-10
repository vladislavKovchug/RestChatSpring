package com.teamdev.chat.impl.repository;

import com.teamdev.chat.repository.TokenRepository;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.Tables;
import com.teamdev.database.entity.Token;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class TokenRepositoryImpl extends AbstractRepository<Token> implements TokenRepository {

    @Override
    protected Tables getTable() {
        return Tables.TOKENS_TABLE;
    }

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

}
