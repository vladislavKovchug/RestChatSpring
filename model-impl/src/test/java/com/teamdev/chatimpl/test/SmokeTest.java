package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.*;
import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.assertEquals;


public class SmokeTest extends AbstractTest {

    @Test
    public void smokeTest() {
        final ChatRoomDTO testChat = chatRoomService.addChatRoom("test_chat");
        userManagementService.register(new RegisterUserDTO("login", "password", new Date()));
        final LoginDTO token = userAuthenticationService.login("login", "password");
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(new UserId(token.userId), new TokenDTO(token.token));
        final ChatRoomDTO firstChat = chatRoomDTOs.iterator().next();

        assertEquals("Wrong chatroom id ", testChat.id, firstChat.id);
        assertEquals("Wrong chatroom name ", testChat.name, firstChat.name);
    }

}
