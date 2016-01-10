package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.*;
import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SmokeTest extends AbstractTest {

    @Test
    public void smokeTest() {
        final ChatRoomDTO testChat = chatRoomService.addChatRoom("test_chat");
        userManagementService.register(new RegisterUserDTO("login", "password", new Date()));
        final LoginDTO user = userAuthenticationService.login("login", "password");
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(new UserId(user.userId), new TokenDTO(user.token));

        boolean chatInList = false;
        for (ChatRoomDTO chatRoom : chatRoomDTOs) {
            chatInList = chatInList || (chatRoom.id == testChat.id);
        }

        assertTrue("test_chat was not found in chat list.", chatInList);
    }

}
