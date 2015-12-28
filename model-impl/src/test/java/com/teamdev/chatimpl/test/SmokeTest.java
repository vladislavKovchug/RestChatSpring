package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.assertEquals;


public class SmokeTest extends AbstractTest {

    private ChatRoomDTO readLastChatRoom(String token) {
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token);
        ChatRoomDTO lastChatRoom = null;
        for (ChatRoomDTO chatRoomDTO : chatRoomDTOs) {
            lastChatRoom = chatRoomDTO;
        }
        return lastChatRoom;
    }

    @Test
    public void smokeTest() {
        chatRoomService.addChatRoom("test_chat");
        userManagementService.register(new RegisterUserDTO("login", "password", 21, new Date()));
        final String token = userAuthenticationService.login("login", "password");
        final ChatRoomDTO chat = readLastChatRoom(token);
        assertEquals("Wrong chatroom name ", "test_chat", chat.name);
    }


}
