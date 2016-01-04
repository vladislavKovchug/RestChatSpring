package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ChatRoomServiceTest extends AbstractTest {

    @Test
    public void testAddingChatRooms() {
        chatRoomService.addChatRoom("add_chat_room");

        final ChatRoomDTO lastChatRoom = readLastChatRoom(new UserId(testUser.userId), new TokenDTO(testUser.token));
        if (lastChatRoom == null) {
            Assert.fail("Chat room wasn't added");
        }
        Assert.assertEquals("last added chat room should be test_chat_room", "add_chat_room", lastChatRoom.name);
    }

    @Test
    public void testFailOnAddingExistingChatRoom() {
        try {
            chatRoomService.addChatRoom("existing_chat_room");
            chatRoomService.addChatRoom("existing_chat_room");
            Assert.fail("Exception should be thrown.");
        } catch (RuntimeException e) {
            Assert.assertEquals("Not correct Exception message.",
                    "Error with create chat room. Chat room with name existing_chat_room already exists.",
                    e.getMessage());
        }
    }

    @Test
    public void testJoinChatRoom() {
        final ChatRoomDTO testChatRoom = chatRoomService.addChatRoom("join_chat_room");
        chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(testUser.token));

        final Iterable<UserProfileDTO> userProfileDTOs =
                chatRoomService.readChatRoomUserList(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                        new TokenDTO(testUser.token));

        final UserProfileDTO chatRoomUser = userProfileDTOs.iterator().next();
        Assert.assertEquals("chat room user should be Test user", testUserProfile.id, chatRoomUser.id);
        Assert.assertEquals("chat room user should be Test user", testUserProfile.name, chatRoomUser.name);
        Assert.assertEquals("chat room user should be Test user", testUserProfile.getBirthday(), chatRoomUser.getBirthday());
    }

    @Test
    public void testFailOnJoinNotExistingChatRoom() {
        try {
            chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(-1),
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "Error with join chat room. Chat room with id -1 not found.",
                    e.getMessage());
        }
    }

    @Test
    public void testFailOnJoinJoinedChatRoom() {
        try {
            final ChatRoomDTO chatRoomDTO = chatRoomService.addChatRoom("joined_chat_room");
            chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                    new TokenDTO(testUser.token));
            chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "Error with join chat room. User is already in current chat room.",
                    e.getMessage());
        }
    }

    @Test
    public void TestLeaveChatRoom() {
        final ChatRoomDTO chatRoomDTO = chatRoomService.addChatRoom("leave_chat_room");
        chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                new TokenDTO(testUser.token));
        chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                new TokenDTO(testUser.token));

        final Iterable<UserProfileDTO> userProfileDTOs =
                chatRoomService.readChatRoomUserList(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                        new TokenDTO(testUser.token));

        for (UserProfileDTO userProfile : userProfileDTOs) {
            if (userProfile.id == testUser.userId) {
                Assert.fail("Found user in leaved chat room.");
            }
        }
    }

    @Test
    public void TestFailOnLeaveNotExistingChatRoom() {
        try {
            chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(-1),
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "Error with leave chat room. Chat room with id -1 not found.",
                    e.getMessage());
        }
    }

    @Test
    public void TestFailOnLeaveLeavedChatRoom() {
        try {
            final ChatRoomDTO chatRoomDTO = chatRoomService.addChatRoom("leaved_chat_room");
            chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                    new TokenDTO(testUser.token));
            chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                    new TokenDTO(testUser.token));
            chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(chatRoomDTO.id),
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "Error with leave chat room. User is not in chat room.",
                    e.getMessage());
        }
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
