package com.teamdev.chat.impl.service;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.repository.TokenRepository;
import com.teamdev.chat.repository.UserRepository;
import com.teamdev.database.entity.Token;
import com.teamdev.database.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.security.AccessControlException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    public static final int FIFTEEN_MINUTES = 60 * 15 * 1000;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenRepository tokenRepository;

    @Override
    public TokenDTO login(String login, String password) {
        HashFunction hf = Hashing.sha256();
        final String passwordHash = hf.newHasher().putString(password, Charsets.UTF_8).hash().toString();
        final List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPasswordHash().equals(passwordHash)) {

                String token = UUID.randomUUID().toString();
                
                tokenRepository.save(new Token(token, user.getId(), //token should expire from 15 minutes
                        new Date(System.currentTimeMillis() + FIFTEEN_MINUTES)));

                return new TokenDTO(user.getId(), token);
            }
        }
        throw new AccessControlException("Access denied.");
    }

    @Override
    public void validateToken(long userId, String token) {
        final Token userToken = tokenRepository.findByToken(token);
        if(userToken == null){
            throw new AccessControlException("Access denied.");
        }

        if(userToken.getExpireTime().compareTo(new Date()) <= 0){
            tokenRepository.delete(userToken);
            throw new AccessControlException("Access denied.");
        }

        if(userToken.getUserId() != userId){
            throw new AccessControlException("Access denied.");
        }
    }

    @Override
    public void logout(long actor, String token) {
        validateToken(actor, token);
        final Token userToken = tokenRepository.findByToken(token);
        tokenRepository.delete(userToken);
    }

}
