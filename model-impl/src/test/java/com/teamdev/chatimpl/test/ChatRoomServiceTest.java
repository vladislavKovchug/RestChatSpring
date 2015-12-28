package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.UserProfileDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChatRoomServiceTest extends AbstractTest {

    private RegisterUserDTO registerUserDTO = new RegisterUserDTO("ivan", "123456", 123, new Date(1700, 10, 10));
    private UserProfileDTO testUser;
    private String testUserToken = "";

    private String RegisterAndLoginAsTestUser() {
        userManagementService.register(registerUserDTO);
        final String token = userAuthenticationService.login(registerUserDTO.login, registerUserDTO.password);
        testUser = userService.readCurrentUserProfile(token);

        return token;
    }

    private ChatRoomDTO readLastChatRoom(String token) {
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token);
        ChatRoomDTO lastChatRoom = null;
        for (ChatRoomDTO chatRoomDTO : chatRoomDTOs) {
            lastChatRoom = chatRoomDTO;
        }
        return lastChatRoom;
    }

    @Before
    public void before() {
        testUserToken = RegisterAndLoginAsTestUser();
    }

    @After
    public void after() {
        userManagementService.deleteUser(testUser.id);
        testUserToken = "";
    }

    @Test
    public void TestAddingChatRooms() {
        chatRoomService.addChatRoom("add_chat_room");

        final ChatRoomDTO lastChatRoom = readLastChatRoom(testUserToken);
        if (lastChatRoom == null) {
            fail("Chat room wasn't added");
        }
        //assertNotEquals("Chat room wasn't added", lastChatRoom, null);

        assertEquals("last added chat room should be test_chat_room", "add_chat_room", lastChatRoom.name);
    }

    @Test
    public void TestFailOnAddingExistingChatRoom() {
        try {
            chatRoomService.addChatRoom("existing_chat_room");
            chatRoomService.addChatRoom("existing_chat_room");
            fail("Exception should be thrown.");
        } catch (RuntimeException e) {
            assertEquals("Not correct Exception message.",
                    "Error with create chat room. Chat room with name existing_chat_room already exists.",
                    e.getMessage());
        }
    }

    @Test
    public void TestJoinChatRoom() {
        chatRoomService.addChatRoom("test_chat_room");
        final ChatRoomDTO lastChatRoom = readLastChatRoom(testUserToken);
        chatRoomService.joinChatRoom(testUserToken, lastChatRoom.id);

        final Iterable<UserProfileDTO> userProfileDTOs =
                chatRoomService.readChatRoomUsersList(testUserToken, lastChatRoom.id);

        final UserProfileDTO chatRoomUser = userProfileDTOs.iterator().next();
        assertEquals("chat room user should be Test user", testUser.id, chatRoomUser.id);
        assertEquals("chat room user should be Test user", testUser.name, chatRoomUser.name);
        assertEquals("chat room user should be Test user", testUser.age, chatRoomUser.age);
        assertEquals("chat room user should be Test user", testUser.getBirthday(), chatRoomUser.getBirthday());
    }

    @Test
    public void TestFailOnJoinNotExistingChatRoom() {
        try {
            chatRoomService.joinChatRoom(testUserToken, 666);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "Error with join chat room. Chat room with id 666 not found.",
                    e.getMessage());
        }
    }

    @Test
    public void TestFailOnJoinJoinedChatRoom() {
        try {
            final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(testUserToken);
            final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();
            chatRoomService.joinChatRoom(testUserToken, chatRoom.id);
            chatRoomService.joinChatRoom(testUserToken, chatRoom.id);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "Error with join chat room. User is already in current chat room.",
                    e.getMessage());
        }
    }

    @Test
    public void TestLeaveChatRoom() {
        chatRoomService.addChatRoom("leave_chat_room");
        final ChatRoomDTO lastChatRoom = readLastChatRoom(testUserToken);
        chatRoomService.joinChatRoom(testUserToken, lastChatRoom.id);
        chatRoomService.leaveChatRoom(testUserToken, lastChatRoom.id);

        final Iterable<UserProfileDTO> userProfileDTOs =
                chatRoomService.readChatRoomUsersList(testUserToken, lastChatRoom.id);

        for (UserProfileDTO userProfile : userProfileDTOs) {
            if (userProfile.id == testUser.id) {
                fail("Found user in leaved chat room.");
            }
        }
    }

    @Test
    public void TestFailOnLeaveNotExistingChatRoom() {
        try {
            chatRoomService.leaveChatRoom(testUserToken, 666);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "Error with leave chat room. Chat room with id 666 not found.",
                    e.getMessage());
        }
    }

    @Test
    public void TestFailOnLeaveLeavedChatRoom() {
        try {
            final ChatRoomDTO lastChatRoom = readLastChatRoom(testUserToken);
            chatRoomService.joinChatRoom(testUserToken, lastChatRoom.id);
            chatRoomService.leaveChatRoom(testUserToken, lastChatRoom.id);
            chatRoomService.leaveChatRoom(testUserToken, lastChatRoom.id);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "Error with leave chat room. User is not in chat room.",
                    e.getMessage());
        }
    }

}
