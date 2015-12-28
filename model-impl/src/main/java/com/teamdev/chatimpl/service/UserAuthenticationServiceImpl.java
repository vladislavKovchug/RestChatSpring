package com.teamdev.chatimpl.service;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chatimpl.repository.TokenRepository;
import com.teamdev.chatimpl.repository.UserRepository;
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

    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenRepository tokenRepository;

    @Override
    public String login(String login, String password) {
        HashFunction hf = Hashing.sha256();
        final String passwordHash = hf.newHasher().putString(password, Charsets.UTF_8).hash().toString();
        final List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPasswordHash().equals(passwordHash)) {

                String token = UUID.randomUUID().toString();
                tokenRepository.save(new Token(token, user.getId(), new Date(System.currentTimeMillis() + 60*15*1000)));

                return token;
            }
        }
        throw new AccessControlException("Access denied.");
    }

    @Override
    public void checkUserLogged(String token) {
        readCurrentUserId(token);
    }

    @Override
    public long readCurrentUserId(String token) {
        final Token userToken = tokenRepository.findByToken(token);
        if(userToken == null){
            throw new AccessControlException("Access denied.");
        }

        if(userToken.getExpireTime().compareTo(new Date()) <= 0){
            tokenRepository.delete(userToken);
            throw new AccessControlException("Access denied.");
        }

        if(userRepository.findOne(userToken.getUserId()) == null){
            tokenRepository.delete(userToken);
            throw new AccessControlException("Access denied.");
        }

        return userToken.getUserId();
    }

    @Override
    public void logout(String token) {
        final Token userToken = tokenRepository.findByToken(token);
        if(userToken == null){
            throw new AccessControlException("Access denied.");
        }

        tokenRepository.delete(userToken);
    }
}
