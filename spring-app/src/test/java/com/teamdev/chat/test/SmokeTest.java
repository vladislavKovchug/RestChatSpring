package com.teamdev.chat.test;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chatimpl.service.UserAuthenticationServiceImpl;
import com.teamdev.database.ChatDatabase;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.util.Date;


public class SmokeTest extends AbstractTest {

    @Test
    public void smokeTest(){
        chatRoomService.addChatRoom("test_chat");
        userManagementService.register(new RegisterUserDTO("login", "password", 21, new Date()));
        final String token = userAuthenticationService.login("login", "password");
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token);
        final ChatRoomDTO chat = chatRoomDTOs.iterator().next();
        Assert.assertEquals("Wrong chatroom name ", "test_chat", chat.name);
    }


}
