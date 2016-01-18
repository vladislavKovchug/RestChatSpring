package com.teamdev.chat.service;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.entity.Token;
import com.teamdev.chat.entity.User;
import com.teamdev.chat.exception.AuthenticationException;
import com.teamdev.chat.repository.TokenRepository;
import com.teamdev.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    public static final int FIFTEEN_MINUTES = 60 * 15 * 1000;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenRepository tokenRepository;

    @Override
    public LoginDTO login(String login, String password) {
        HashFunction hf = Hashing.sha256();
        final String passwordHash = hf.newHasher().putString(password, Charsets.UTF_8).hash().toString();
        final Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPasswordHash().equals(passwordHash)) {

                String token = UUID.randomUUID().toString();
                while (tokenRepository.findByToken(token) != null){
                    token = UUID.randomUUID().toString();
                }

                tokenRepository.save(new Token(token, user, //token should expire from 15 minutes
                        new Date(System.currentTimeMillis() + FIFTEEN_MINUTES)));

                return new LoginDTO(user.getId(), token);
            }
        }
        throw new AuthenticationException("Login Failed.");
    }

    @Override
    public void validateToken(UserId userId, TokenDTO token) {
        final Token userToken = tokenRepository.findByToken(token.token);
        if(userToken == null){
            throw new AuthenticationException("Access denied.");
        }

        if(userToken.getUser().getId() != userId.id){
            throw new AuthenticationException("Access denied.");
        }

        if(userToken.getExpireTime().compareTo(new Date()) <= 0){
            tokenRepository.delete(userToken);
            throw new AuthenticationException("Token has been expired.");
        }

        if (userRepository.findOne(userId.id) == null) {
            tokenRepository.delete(userToken);
            throw new AuthenticationException("Access denied.");
        }

    }

    @Override
    public void logout(UserId actor, TokenDTO token) {
        final Token userToken = tokenRepository.findByToken(token.token);
        tokenRepository.delete(userToken);
    }

}
