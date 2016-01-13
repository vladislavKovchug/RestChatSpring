package com.teamdev.chat.test.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.request.AddChatRoomRequest;
import com.teamdev.chat.request.TokenRequest;
import com.teamdev.chat.test.exception.HttpRequestFailedException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ChatRoomIntegrationTest extends IntegrationTest {


    @Test
    public void testReadAllChatRooms() {
        final LoginDTO loginDTO = loginAsTestUser();
        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);

        boolean firstChatExists = false;
        boolean secondChatExists = false;
        for (ChatRoomDTO chatRoomDTO : chatRoomDTOs) {
            if (chatRoomDTO.id == 1 && "chat".equals(chatRoomDTO.name)) {
                firstChatExists = true;
            }
            if (chatRoomDTO.id == 2 && "chat 2".equals(chatRoomDTO.name)) {
                secondChatExists = true;
            }
        }

        Assert.assertTrue("First chat was not found in response.", firstChatExists);
        Assert.assertTrue("Second chat was not found in response.", secondChatExists);
    }

    @Test
    public void testReadAllChatRoomsFailsOnInvalidToken() {
        try {
            final HttpUriRequest getChatRoomsRequest = RequestBuilder.get(CHAT_URL + "/chats" + "/1")
                    .addParameter(TOKEN_PARAMETER_NAME, "some-token")
                    .build();
            doRequest(getChatRoomsRequest);
            Assert.fail("Error, expected exception throw.");
        } catch (HttpRequestFailedException e) {
            Assert.assertEquals("Error, wrong status code.", 403, e.getStatusLine().getStatusCode());
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }
    }

    @Test
    public void testCreateChatRoom() {
        final String newChatName = "new chat room";

        final HttpUriRequest createChatRoomsRequest = addJsonParameters(RequestBuilder.post(CHAT_URL + "/chats"),
                new AddChatRoomRequest(newChatName))
                .build();

        String response = doRequestWithAssert(createChatRoomsRequest);

        final JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        final long id = jsonObject.get("id").getAsLong();
        final String name = jsonObject.get("name").getAsString();

        final HttpUriRequest deleteChatRoomsRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + id)
                .build();

        doRequestWithAssert(deleteChatRoomsRequest);

        Assert.assertEquals("Created chat room name does not match", newChatName, name);

    }

    @Test
    public void testDeleteChatRoom() {
        final String newChatName = "sample chat";

        final HttpUriRequest createChatRoomsRequest = addJsonParameters(RequestBuilder.post(CHAT_URL + "/chats"),
                new AddChatRoomRequest(newChatName))
                .build();

        String response = doRequestWithAssert(createChatRoomsRequest);

        final JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        final long id = jsonObject.get("id").getAsLong();
        final String name = jsonObject.get("name").getAsString();

        final HttpUriRequest deleteChatRoomsRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + id)
                .build();

        doRequestWithAssert(deleteChatRoomsRequest);

        final LoginDTO loginDTO = loginAsTestUser();
        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        boolean chatRoomExists = false;

        for (ChatRoomDTO chatRoomDTO : chatRoomDTOs) {
            if (newChatName.equals(chatRoomDTO.name)) {
                chatRoomExists = true;
            }
        }

        Assert.assertFalse("Error chat room exists after delete.", chatRoomExists);
    }

    @Test
    public void testJoinAndLeaveChatRoom() {
        final LoginDTO loginDTO = loginAsTestUser();
        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest joinChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId),
                new TokenRequest(loginDTO.token)).build();

        doRequestWithAssert(joinChatRoomRequest);

        final HttpUriRequest getChatRoomUsersRequest = RequestBuilder.get(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        String usersResponse = doRequestWithAssert(getChatRoomUsersRequest);

        boolean isUserInList = false;
        JsonArray jsonArray = new JsonParser().parse(usersResponse).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            final JsonObject jsonObject = jsonElement.getAsJsonObject();
            isUserInList = isUserInList || USER_LOGIN.equals(jsonObject.get("name").getAsString());
        }

        Assert.assertTrue("Error, user should be in chat room after join.", isUserInList);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        doRequestWithAssert(leaveChatRoomRequest);

        usersResponse = doRequestWithAssert(getChatRoomUsersRequest);

        isUserInList = false;
        jsonArray = new JsonParser().parse(usersResponse).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            final JsonObject jsonObject = jsonElement.getAsJsonObject();
            isUserInList = isUserInList || USER_LOGIN.equals(jsonObject.get("name").getAsString());
        }

        Assert.assertFalse("Error, user should not be in chat room after leave.", isUserInList);
    }

}
