package com.teamdev.chat.test;


import com.teamdev.chat.dto.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class MessageServiceTest extends AbstractTest {

    @Test
    public void testPostingMessageToChatRoom(){
        chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(testUser.token));

        messageService.sendMessage(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id), "test message",
                new TokenDTO(testUser.token));
        final MessageDTO lastMessage = readLastMessage(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(testUser.token));
        chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(testUser.token));

        Assert.assertEquals("last message should be from Test user", testUserProfile.id, lastMessage.fromUserId);
        Assert.assertEquals("last message should be from Test user", testUserProfile.name, lastMessage.fromUserName);
        Assert.assertEquals("message text should be equals", "test message", lastMessage.message);
    }

    @Test
    public void testFailsPostingMessageToNotExistingChatRoom(){
        try {
            messageService.sendMessage(new UserId(testUser.userId), new ChatRoomId(-1), "test message",
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "No chat room with id -1 found.",
                    e.getMessage());
        }
    }

    @Test
    public void testFailsPostingMessageToNotJoinedChatRoom(){
        try {
            messageService.sendMessage(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id), "test message",
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "Error send message to not joined chat room.",
                    e.getMessage());
        }
    }

    @Test
    public void testPostingPrivateMessage(){
        userManagementService.register(new RegisterUserDTO("ivan2", "1", new Date()));
        final LoginDTO ivan2 = userAuthenticationService.login("ivan2", "1");
        UserProfileDTO receiverUser = userService.readUserProfile(new UserId(ivan2.userId), new UserId(ivan2.userId),
                new TokenDTO(ivan2.token));


        chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(testUser.token));
        chatRoomService.joinChatRoom(new UserId(ivan2.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(ivan2.token));
        messageService.sendPrivateMessage(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                "test private message", new UserId(ivan2.userId), new TokenDTO(testUser.token));
        final MessageDTO lastMessage = readLastMessage(new UserId(ivan2.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(ivan2.token));
        chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(testUser.token));
        chatRoomService.leaveChatRoom(new UserId(ivan2.userId), new ChatRoomId(testChatRoom.id),
                new TokenDTO(ivan2.token));

        userManagementService.deleteUser(new UserId(ivan2.userId));
        Assert.assertEquals("last message should be from Test user", testUserProfile.id, lastMessage.fromUserId);
        Assert.assertEquals("last message should be from Test user", testUserProfile.name, lastMessage.fromUserName);
        Assert.assertEquals("last message should be To user with id " + Long.toString(receiverUser.id),
                receiverUser.id, lastMessage.toUserId);
        Assert.assertEquals("last message should be To user with name " + receiverUser.name, receiverUser.name,
                lastMessage.toUserName);
        Assert.assertEquals("last message should be private", true, lastMessage.privateMessage);
        Assert.assertEquals("message text should be equals", "test private message", lastMessage.message);
    }

    @Test
    public void testFailPostingPrivateMessageToUserNotInChatRoom(){
        try{
            chatRoomService.joinChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                    new TokenDTO(testUser.token));
            messageService.sendPrivateMessage(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                    "test private message", new UserId(-1), new TokenDTO(testUser.token));
            chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                    new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e){
            chatRoomService.leaveChatRoom(new UserId(testUser.userId), new ChatRoomId(testChatRoom.id),
                    new TokenDTO(testUser.token));
            Assert.assertEquals("Not correct Exception message.", "Error send message to user that not joined chat room.",
                    e.getMessage());
        }
    }

    private MessageDTO readLastMessage(UserId userId, ChatRoomId chatRoomId, TokenDTO token){
        final Iterable<MessageDTO> messageDTOs = messageService.readChatRoomMessages(userId, chatRoomId, 0, token);
        MessageDTO lastMessage = null;
        for (MessageDTO messageDTO : messageDTOs) {
            lastMessage = messageDTO;
        }
        return lastMessage;
    }

/*
    private RegisterUserDTO registerUserDTO = new RegisterUserDTO("ivan", "123456", 123, new Date(1700, 10, 10));
    private UserProfileDTO testUser;
    private String testUserToken = "";
    private ChatRoomDTO chatRoom;
    private boolean initialize = true;


    private ChatRoomDTO readLastChatRoom(String token) {
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token);
        ChatRoomDTO lastChatRoom = null;
        for (ChatRoomDTO chatRoomDTO : chatRoomDTOs) {
            lastChatRoom = chatRoomDTO;
        }
        return lastChatRoom;
    }



    private String RegisterAndLoginAsTestUser() {
        userManagementService.register(registerUserDTO);
        final String token = userAuthenticationService.login(registerUserDTO.login, registerUserDTO.password);
        testUser = userService.readCurrentUserProfile(token);

        return token;
    }

    @Before
    public void before(){
        if(initialize){
            chatRoomService.addChatRoom("chat_testing");
            initialize = false;
        }

        testUserToken = RegisterAndLoginAsTestUser();
        chatRoom = readLastChatRoom(testUserToken);
    }

    @After
    public void after(){
        userManagementService.deleteUser(testUser.id);
        chatRoomService.deleteChatRoom("chat_testing");
        testUserToken = "";
    }


*/
}
