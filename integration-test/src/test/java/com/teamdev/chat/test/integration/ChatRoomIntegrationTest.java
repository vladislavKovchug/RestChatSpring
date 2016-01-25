package com.teamdev.chat.test.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.request.AddChatRoomRequest;
import com.teamdev.chat.request.TokenRequest;
import com.teamdev.chat.response.ErrorResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

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
    public void testCreateExistingChatRoomFail() {
        final String newChatName = "new chat room";

        final HttpUriRequest createChatRoomsRequest = addJsonParameters(RequestBuilder.post(CHAT_URL + "/chats"),
                new AddChatRoomRequest(newChatName))
                .build();

        String response = doRequestWithAssert(createChatRoomsRequest);

        final ErrorResponse errorResponse = doErrorRequest(createChatRoomsRequest);

        final JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        final long id = jsonObject.get("id").getAsLong();

        final HttpUriRequest deleteChatRoomsRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + id)
                .build();

        doRequestWithAssert(deleteChatRoomsRequest);

        Assert.assertEquals("Error, wrong status code on create existing chatRoom.",
                "Error with create chat room. Chat room with name new chat room already exists.", errorResponse.errorMessage);
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
    public void testDeleteNotExistingChatRoomFails() {
        final HttpUriRequest deleteChatRoomsRequest = RequestBuilder.delete(CHAT_URL + "/chats/-1")
                .build();

        final ErrorResponse errorResponse = doErrorRequest(deleteChatRoomsRequest);

        Assert.assertEquals("Error wrong status code on delete not existing user.",
                "Error delete not existed chat room with id -1", errorResponse.errorMessage);
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

    @Test
    public void testJoinNotExistedChatRoomFail() {
        final LoginDTO loginDTO = loginAsTestUser();

        final HttpUriRequest joinChatRoomRequest = addJsonParameters(
                RequestBuilder.put(CHAT_URL + "/chats/-1/" + loginDTO.userId),
                new TokenRequest(loginDTO.token)).build();

        final ErrorResponse errorResponse = doErrorRequest(joinChatRoomRequest);
        Assert.assertEquals("Error wrong status code on join not existed chat room.",
                "Error with join chat room. Chat room with id -1 not found.", errorResponse.errorMessage);
    }

    @Test
    public void testJoinJoinedChatRoomFail() {
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
        final ErrorResponse errorResponse = doErrorRequest(joinChatRoomRequest);

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        doRequestWithAssert(leaveChatRoomRequest);

        Assert.assertEquals("Error wrong status code on join joined chat room.",
                "Error with join chat room. User is already in current chat room.", errorResponse.errorMessage);
    }

    @Test
    public void testLeaveNotExistedChatRoomFail() {
        final LoginDTO loginDTO = loginAsTestUser();

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/-1/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        final ErrorResponse errorResponse = doErrorRequest(leaveChatRoomRequest);
        Assert.assertEquals("Error wrong status code on leave not existed chat room.",
                "Error with leave chat room. Chat room with id -1 not found.", errorResponse.errorMessage);
    }

    @Test
    public void testLeaveNotJoinedChatRoomFail() {
        final LoginDTO loginDTO = loginAsTestUser();
        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        if (!chatRoomDTOs.iterator().hasNext()) {
            Assert.fail("No chat rooms to join");
        }

        final ChatRoomDTO chatRoom = chatRoomDTOs.iterator().next();

        final HttpUriRequest leaveChatRoomRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + chatRoom.id + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        final ErrorResponse errorResponse = doErrorRequest(leaveChatRoomRequest);

        Assert.assertEquals("Error wrong status code on leave not joined chat room.",
                "Error with leave chat room. User is not in chat room.", errorResponse.errorMessage);
    }

    @Test
    public void testReadUserListOfNotExistedChatRoomFails() {
        final LoginDTO loginDTO = loginAsTestUser();
        final HttpUriRequest getChatRoomUsersRequest = RequestBuilder.get(CHAT_URL + "/chats/-1/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        final ErrorResponse errorResponse = doErrorRequest(getChatRoomUsersRequest);

        Assert.assertEquals("Error wrong status code on read not existed chat room user list.",
                "Error with read chat room users. Chat room with id -1 not found.", errorResponse.errorMessage);
    }

}
