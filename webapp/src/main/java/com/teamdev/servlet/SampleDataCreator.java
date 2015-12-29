package com.teamdev.servlet;


import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.MessageService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserManagementService;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public class SampleDataCreator {

    public void createSampleData(ApplicationContext applicationContext){
        final UserManagementService userManagementService =
                applicationContext.getBean(UserManagementService.class);
        userManagementService.register(new RegisterUserDTO("user1", "12345", new Date()));
        userManagementService.register(new RegisterUserDTO("user2", "big_password123", new Date()));

        final ChatRoomService chatRoomService =
                applicationContext.getBean(ChatRoomService.class);
        chatRoomService.addChatRoom("chat");
        chatRoomService.addChatRoom("chat 2");

        final UserAuthenticationService userAuthenticationService =
                applicationContext.getBean(UserAuthenticationService.class);

        final TokenDTO userToken = userAuthenticationService.login("user1", "12345");

        chatRoomService.joinChatRoom(userToken.userId, 1, userToken.token);
        final MessageService messageService =
                applicationContext.getBean(MessageService.class);
        messageService.sendMessage(userToken.userId, 1, "hello", userToken.token);
    }

}
