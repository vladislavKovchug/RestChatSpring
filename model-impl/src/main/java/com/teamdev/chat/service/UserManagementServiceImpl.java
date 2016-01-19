package com.teamdev.chat.service;


import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.entity.Message;
import com.teamdev.chat.entity.User;
import com.teamdev.chat.exception.UserException;
import com.teamdev.chat.repository.MessageRepository;
import com.teamdev.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private UserAuthenticationService userAuthenticationService;

    @Override
    public UserProfileDTO register(RegisterUserDTO registerUserDTO) {
        HashFunction hf = Hashing.sha256();
        String passwordHash = hf.newHasher().putString(registerUserDTO.password, Charsets.UTF_8).hash().toString();
        if (userRepository.findUserByLogin(registerUserDTO.login) != null) {
            throw new UserException("User " + registerUserDTO.login + " already exists.");
        }
        final User user = new User(registerUserDTO.login, passwordHash,
                registerUserDTO.getBirthday());
        userRepository.save(user);
        return  new UserProfileDTO(user.getId(), user.getLogin(), user.getBirthday());
    }

    @Override
    public void deleteUser(UserId userId) {
        User user = userRepository.findOne(userId.id);
        if(user == null){
            throw new UserException("User with id " + Long.toString(userId.id) + " does not exists.");
        }

        for(Message message: user.getSentMessages()){
            message.setUserFrom(null);
        }

        for(Message message: user.getPrivateMessages()){
            messageRepository.delete(message);
        }

        userRepository.delete(user.getId());
    }

}
