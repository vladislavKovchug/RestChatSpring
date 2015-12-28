package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.assertEquals;


public class SmokeTest extends AbstractTest {

    @Test
    public void smokeTest() {
        chatRoomService.addChatRoom("test_chat");
        userManagementService.register(new RegisterUserDTO("login", "password", 21, new Date()));
        final String token = userAuthenticationService.login("login", "password");
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token);
        final ChatRoomDTO chat = chatRoomDTOs.iterator().next();
        assertEquals("Wrong chatroom name ", "test_chat", chat.name);
    }


}
