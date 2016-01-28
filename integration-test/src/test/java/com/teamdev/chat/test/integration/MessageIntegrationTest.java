package com.teamdev.chat.test.integration;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.request.PostMessageRequest;
import com.teamdev.chat.request.TokenRequest;
import com.teamdev.chat.response.ErrorResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MessageIntegrationTest extends IntegrationTest {

    public static final String USER2_LOGIN = "user2";
    public static final String USER2_PASSWORD = "12345";

    @Test
    public void testPostPublicMessage() {
        final LoginDTO loginDTO = loginAsTestUser();
        final String message = "hello";

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest joinChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId),
                new TokenRequest(loginDTO.token)).build();

        doRequestWithAssert(joinChatRoomRequest);

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id + "/" + loginDTO.userId),
                        new PostMessageRequest(message, loginDTO.token)).build();

        final String messageResponse = doRequestWithAssert(postMessageRequest);
        final JsonObject jsonMessage = new JsonParser().parse(messageResponse).getAsJsonObject();
        final String sentMessageText = jsonMessage.get("message").getAsString();
        final long sentMessageId = jsonMessage.get("id").getAsLong();

        Assert.assertEquals("Messages Text does not match", message, sentMessageText);

        boolean messageInChatRoom = false;
        final JsonArray messages = readChatRoomMessages(loginDTO, chatRoom.id);
        for (JsonElement jsonElement : messages) {
            messageInChatRoom = messageInChatRoom || jsonElement.getAsJsonObject().get("id").getAsLong() == sentMessageId;
        }
        Assert.assertTrue("Error, public message was not found", messageInChatRoom);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();
        doRequestWithAssert(leaveChatRoomRequest);
    }

    @Test
    public void testPostTwoPublicMessages() {
        final LoginDTO loginDTO = loginAsTestUser();
        final String message = "hello";

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest joinChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId),
                new TokenRequest(loginDTO.token)).build();

        doRequestWithAssert(joinChatRoomRequest);

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id + "/" + loginDTO.userId),
                        new PostMessageRequest(message, loginDTO.token)).build();

        doRequestWithAssert(postMessageRequest);
        doRequestWithAssert(postMessageRequest);

        boolean messageInChatRoom = false;
        long messagesCount = 0;
        final JsonArray messages = readChatRoomMessages(loginDTO, chatRoom.id);
        for (JsonElement jsonElement : messages) {
            messagesCount++;
        }
        Assert.assertTrue("Error there is less than 2 messages in chatroom", messagesCount >= 2);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();
        doRequestWithAssert(leaveChatRoomRequest);
    }

    @Test
    public void testPostPrivateMessage() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser(USER2_LOGIN, USER2_PASSWORD);
        final String message = "pss.. do you want some force push ?";

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(testUserLoginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }
        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest joinFirstUserToChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/" + chatRoom.id + "/" + testUserLoginDTO.userId),
                new TokenRequest(testUserLoginDTO.token)).build();
        doRequestWithAssert(joinFirstUserToChatRoomRequest);

        final HttpUriRequest joinSecondUserToChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/" + chatRoom.id + "/" + secondUserLoginDTO.userId),
                new TokenRequest(secondUserLoginDTO.token)).build();
        doRequestWithAssert(joinSecondUserToChatRoomRequest);

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id +
                                "/" + testUserLoginDTO.userId + "/" + secondUserLoginDTO.userId),
                        new PostMessageRequest(message, testUserLoginDTO.token)).build();

        final String messageResponse = doRequestWithAssert(postMessageRequest);
        final JsonObject jsonMessage = new JsonParser().parse(messageResponse).getAsJsonObject();
        final String sentMessageText = jsonMessage.get("message").getAsString();
        final long sentMessageId = jsonMessage.get("id").getAsLong();

        Assert.assertEquals("Messages Text does not match", message, sentMessageText);

        boolean messageInChatRoom = false;
        final JsonArray messages = readChatRoomMessages(secondUserLoginDTO, chatRoom.id);
        for (JsonElement jsonElement : messages) {
            messageInChatRoom = messageInChatRoom || jsonElement.getAsJsonObject().get("id").getAsLong() == sentMessageId;
        }
        Assert.assertTrue("Error, private message was not found", messageInChatRoom);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + testUserLoginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, testUserLoginDTO.token)
                .build();
        doRequestWithAssert(leaveChatRoomRequest);
        final HttpUriRequest leaveChatRoomSecondRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + secondUserLoginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, secondUserLoginDTO.token)
                .build();
        doRequestWithAssert(leaveChatRoomSecondRequest);
    }

    @Test
    public void testReadNotExistingChatRoomMessagesFail() {
        final LoginDTO loginDTO = loginAsTestUser();
        final HttpUriRequest getMessagesRequest =
                RequestBuilder.get(CHAT_URL + "/messages/-1/" + loginDTO.userId + "/0")
                        .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                        .build();
        final ErrorResponse errorResponse = doErrorRequest(getMessagesRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.",
                "Error with getting chat room messages. Chat room with id -1 not found.", errorResponse.errorMessage);
    }

    @Test
    public void testPostingPublicMessageToNotExistingChatRoomFail() {
        final LoginDTO loginDTO = loginAsTestUser();

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/-1/" + loginDTO.userId),
                        new PostMessageRequest("some message", loginDTO.token)).build();

        final ErrorResponse errorResponse = doErrorRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on post public message to not existed chat room.",
                "No chat room with id -1 found.", errorResponse.errorMessage);
    }

    @Test
    public void testPostingPublicMessageToNotJoinedChatRoomFail() {
        final LoginDTO loginDTO = loginAsTestUser();

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id + "/" + loginDTO.userId),
                        new PostMessageRequest("some message", loginDTO.token)).build();

        final ErrorResponse errorResponse = doErrorRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on post public message to not joined chat room.",
                "Error send message to not joined chat room.", errorResponse.errorMessage);
    }

    @Test
    public void testPostingPrivateMessageToNotExistingChatRoomFail() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser(USER2_LOGIN, USER2_PASSWORD);

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/-1" +
                                "/" + testUserLoginDTO.userId + "/" + secondUserLoginDTO.userId),
                        new PostMessageRequest("some message", testUserLoginDTO.token)).build();

        final ErrorResponse errorResponse = doErrorRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.",
                "No chat room with id -1 found.", errorResponse.errorMessage);
    }

    @Test
    public void testPostingPrivateMessageToNotJoinedChatRoomFail() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser(USER2_LOGIN, USER2_PASSWORD);

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(testUserLoginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id +
                                "/" + testUserLoginDTO.userId + "/" + secondUserLoginDTO.userId),
                        new PostMessageRequest("some message", testUserLoginDTO.token)).build();

        final ErrorResponse errorResponse = doErrorRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.",
                "Error send message to not joined chat room.", errorResponse.errorMessage);
    }

    @Test
    public void testPostingPrivateMessageToUserNotInChatRoomFail() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser(USER2_LOGIN, USER2_PASSWORD);

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(testUserLoginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest joinFirstUserToChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/" + chatRoom.id + "/" + testUserLoginDTO.userId),
                new TokenRequest(testUserLoginDTO.token)).build();
        doRequestWithAssert(joinFirstUserToChatRoomRequest);

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id +
                                "/" + testUserLoginDTO.userId + "/" + secondUserLoginDTO.userId),
                        new PostMessageRequest("some message", testUserLoginDTO.token)).build();

        final ErrorResponse errorResponse = doErrorRequest(postMessageRequest);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + testUserLoginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, testUserLoginDTO.token)
                .build();
        doRequestWithAssert(leaveChatRoomRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.",
                "Error send message to user that not joined chat room.", errorResponse.errorMessage);
    }

    private JsonArray readChatRoomMessages(LoginDTO loginDTO, long chatRoomId) {
        final HttpUriRequest getMessagesRequest =
                RequestBuilder.get(CHAT_URL + "/messages/" + chatRoomId + "/" + loginDTO.userId + "/0")
                        .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                        .build();
        String messagesResponse = doRequestWithAssert(getMessagesRequest);
        return new JsonParser().parse(messagesResponse).getAsJsonArray();
    }

}
