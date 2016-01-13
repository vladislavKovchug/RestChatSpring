package com.teamdev.chat;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.MessageService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserManagementService;

import javax.inject.Inject;
import java.util.Date;

public class SampleDataCreator {

    @Inject
    UserManagementService userManagementService;

    @Inject
    ChatRoomService chatRoomService;

    @Inject
    UserAuthenticationService userAuthenticationService;

    @Inject
    MessageService messageService;

    public void createSampleData(){
        userManagementService.register(new RegisterUserDTO("user1", "12345", new Date()));
        userManagementService.register(new RegisterUserDTO("user2", "big_password123", new Date()));

        final ChatRoomDTO chat1 = chatRoomService.addChatRoom("chat");
        final ChatRoomDTO chat2 = chatRoomService.addChatRoom("chat 2");

    }

}