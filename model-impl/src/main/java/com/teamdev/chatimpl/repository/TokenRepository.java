package com.teamdev.chatimpl.repository;


import com.teamdev.database.entity.Token;

public interface TokenRepository extends Repository<Token> {
    Token findByToken(String token);
}
