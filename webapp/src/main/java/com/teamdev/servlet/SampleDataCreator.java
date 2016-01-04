package com.teamdev.servlet;


import com.teamdev.chat.dto.*;
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
        final ChatRoomDTO chat1 = chatRoomService.addChatRoom("chat");
        final ChatRoomDTO chat2 = chatRoomService.addChatRoom("chat 2");

        final UserAuthenticationService userAuthenticationService =
                applicationContext.getBean(UserAuthenticationService.class);

        final LoginDTO userToken = userAuthenticationService.login("user1", "12345");

        chatRoomService.joinChatRoom(new UserId(userToken.userId), new ChatRoomId(chat1.id), new TokenDTO(userToken.token));
        final MessageService messageService =
                applicationContext.getBean(MessageService.class);
        messageService.sendMessage(new UserId(userToken.userId), new ChatRoomId(chat1.id), "hello", new TokenDTO(userToken.token));
    }

}
