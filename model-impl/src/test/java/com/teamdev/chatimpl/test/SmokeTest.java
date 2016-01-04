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
        final LoginDTO user = userAuthenticationService.login("login", "password");
        final ChatRoomDTO lastChat = readLastChatRoom(new UserId(user.userId), new TokenDTO(user.token));

        assertEquals("Wrong chatroom id ", testChat.id, lastChat.id);
        assertEquals("Wrong chatroom name ", testChat.name, lastChat.name);
    }

    private ChatRoomDTO readLastChatRoom(UserId userId, TokenDTO token) {
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(userId, token);
        ChatRoomDTO lastChatRoom = null;
        for (ChatRoomDTO chatRoomDTO : chatRoomDTOs) {
            lastChatRoom = chatRoomDTO;
        }
        return lastChatRoom;
    }

}
