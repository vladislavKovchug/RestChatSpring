package com.teamdev.chat.test.integration;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.request.PostMessageRequest;
import com.teamdev.chat.request.TokenRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MessageIntegrationTest extends IntegrationTest {

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
    public void testPostPrivateMessage() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser("user2", "big_password123");
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
        final StatusLine statusLine = doFailRequest(getMessagesRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.", 500, statusLine.getStatusCode());
    }

    @Test
    public void testPostingPublicMessageToNotExistingChatRoomFail() {
        final LoginDTO loginDTO = loginAsTestUser();

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/-1/" + loginDTO.userId),
                        new PostMessageRequest("some message", loginDTO.token)).build();

        final StatusLine statusLine = doFailRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on post public message to not existed chat room.", 500, statusLine.getStatusCode());
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

        final StatusLine statusLine = doFailRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on post public message to not joined chat room.", 500, statusLine.getStatusCode());
    }

    @Test
    public void testPostingPrivateMessageToNotExistingChatRoomFail() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser("user2", "big_password123");

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/-1" +
                                "/" + testUserLoginDTO.userId + "/" + secondUserLoginDTO.userId),
                        new PostMessageRequest("some message", testUserLoginDTO.token)).build();

        final StatusLine statusLine = doFailRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.", 500, statusLine.getStatusCode());
    }

    @Test
    public void testPostingPrivateMessageToNotJoinedChatRoomFail() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser("user2", "big_password123");

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(testUserLoginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest postMessageRequest =
                addJsonParameters(RequestBuilder.post(CHAT_URL + "/messages/" + chatRoom.id +
                                "/" + testUserLoginDTO.userId + "/" + secondUserLoginDTO.userId),
                        new PostMessageRequest("some message", testUserLoginDTO.token)).build();

        final StatusLine statusLine = doFailRequest(postMessageRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.", 500, statusLine.getStatusCode());
    }

    @Test
    public void testPostingPrivateMessageToUserNotInChatRoomFail() {
        final LoginDTO testUserLoginDTO = loginAsTestUser();
        final LoginDTO secondUserLoginDTO = loginUser("user2", "big_password123");

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

        final StatusLine statusLine = doFailRequest(postMessageRequest);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + testUserLoginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, testUserLoginDTO.token)
                .build();
        doRequestWithAssert(leaveChatRoomRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room messages.", 500, statusLine.getStatusCode());
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
