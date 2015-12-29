package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.TokenDTO;
import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.assertEquals;


public class SmokeTest extends AbstractTest {

    @Test
    public void smokeTest() {
        final ChatRoomDTO testChat = chatRoomService.addChatRoom("test_chat");
        userManagementService.register(new RegisterUserDTO("login", "password", new Date()));
        final TokenDTO token = userAuthenticationService.login("login", "password");
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token.userId, token.token);
        final ChatRoomDTO firstChat = chatRoomDTOs.iterator().next();

        assertEquals("Wrong chatroom id ", testChat.id, firstChat.id);
        assertEquals("Wrong chatroom name ", testChat.name, firstChat.name);
    }

}
