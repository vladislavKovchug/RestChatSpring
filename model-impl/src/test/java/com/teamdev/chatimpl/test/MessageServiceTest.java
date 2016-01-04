package com.teamdev.chatimpl.test;


import org.junit.Test;

public class MessageServiceTest extends AbstractTest {

    @Test
    public void someTest(){

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

    private MessageDTO readLastMessage(String token){
        final Iterable<MessageDTO> messageDTOs = messageService.readChatRoomMessages(testUserToken, chatRoom.id, 0);
        MessageDTO lastMessage = null;
        for (MessageDTO messageDTO : messageDTOs) {
            lastMessage = messageDTO;
        }
        return lastMessage;
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

    @Test
    public void testPostingMessageToChatRoom(){
        chatRoomService.joinChatRoom(testUserToken, chatRoom.id);

        messageService.sendMessage(testUserToken, chatRoom.id, "test message");
        final MessageDTO lastMessage = readLastMessage(testUserToken);
        chatRoomService.leaveChatRoom(testUserToken, chatRoom.id);

        assertEquals("last message should be from Test user", testUser.id, lastMessage.fromUserId);
        assertEquals("last message should be from Test user", testUser.name, lastMessage.fromUserName);
        assertEquals("message text should be equals", "test message", lastMessage.message);
    }

    @Test
    public void testFailsPostingMessageToNotExistingChatRoom(){
        try {
           messageService.sendMessage(testUserToken, 666, "test message");
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "No chat room with id 666 found.",
                    e.getMessage());
        }
    }

    @Test
    public void testFailsPostingMessageToNotJoinedChatRoom(){
        try {
            messageService.sendMessage(testUserToken, chatRoom.id, "test message");
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "Error send message to not joined chat room.",
                    e.getMessage());
        }
    }

    @Test
    public void testPostingPrivateMessage(){
        userManagementService.register(new RegisterUserDTO("ivan2", "1", 1, new Date()));
        final String receiverUserToken = userAuthenticationService.login("ivan2", "1");
        UserProfileDTO receiverUser = userService.readCurrentUserProfile(receiverUserToken);


        chatRoomService.joinChatRoom(testUserToken, chatRoom.id);
        chatRoomService.joinChatRoom(receiverUserToken, chatRoom.id);
        messageService.sendPrivateMessage(testUserToken, chatRoom.id,
                "test private message", receiverUser.id);
        final MessageDTO lastMessage = readLastMessage(testUserToken);
        chatRoomService.leaveChatRoom(testUserToken, chatRoom.id);
        chatRoomService.leaveChatRoom(receiverUserToken, chatRoom.id);

        assertEquals("last message should be from Test user", testUser.id, lastMessage.fromUserId);
        assertEquals("last message should be from Test user", testUser.name, lastMessage.fromUserName);
        assertEquals("last message should be To user with id " + Long.toString(receiverUser.id), receiverUser.id,
                lastMessage.toUserId);
        assertEquals("last message should be To user with name " + receiverUser.name, receiverUser.name,
                lastMessage.toUserName);
        assertEquals("last message should be private", true, lastMessage.privateMessage);
        assertEquals("last message should be from Test user", testUser.name, lastMessage.fromUserName);
        assertEquals("message text should be equals", "test private message", lastMessage.message);
    }

    @Test
    public void testFailPostingPrivateMessageToUserNotInChatRoom(){
        try{
            chatRoomService.joinChatRoom(testUserToken, chatRoom.id);
            messageService.sendPrivateMessage(testUserToken, chatRoom.id,
                    "test private message", 1);
            chatRoomService.leaveChatRoom(testUserToken, chatRoom.id);
            fail("Exception should be thrown.");
        } catch (Exception e){
            chatRoomService.leaveChatRoom(testUserToken, chatRoom.id);
            assertEquals("Not correct Exception message.", "Error send message to user that not joined chat room.",
                    e.getMessage());
        }
    }
*/
}
