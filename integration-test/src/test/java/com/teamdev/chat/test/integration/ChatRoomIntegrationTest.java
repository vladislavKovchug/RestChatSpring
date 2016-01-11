package com.teamdev.chat.test.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.test.exception.HttpRequestException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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
    public void testReadAllChatRoomsFailsOnInvalidToken(){
        try{
            final HttpUriRequest getChatRoomsRequest = RequestBuilder.get(CHAT_URL + "/chats" + "/1")
                    .addParameter(TOKEN_PARAMETER_NAME, "some-token")
                    .build();
            doRequest(getChatRoomsRequest);
        } catch (HttpRequestException e){
            Assert.assertEquals("Error, wrong status code.", 403, e.getStatusLine().getStatusCode());
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }
    }

    @Test
    public void testCreateChatRoom(){
        final String newChatName = "new chat room";

        final HttpUriRequest createChatRoomsRequest = RequestBuilder.post(CHAT_URL + "/chats")
                .addParameter("name", newChatName)
                .build();

        String response = "";
        try {
            response = doRequest(createChatRoomsRequest);
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }

        Assert.assertEquals("Created chat room does not match", "{\"id\":3,\"name\":\"new chat room\"}", response);
    }

    @Test
    public void testDeleteChatRoom(){
        final String newChatName = "chat_room_for_delete@@@___";

        final HttpUriRequest createChatRoomsRequest = RequestBuilder.post(CHAT_URL + "/chats")
                .addParameter("name", newChatName)
                .build();

        String response = "";
        try {
            response = doRequest(createChatRoomsRequest);
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }

        final JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        final long id = jsonObject.get("id").getAsLong();
        final String name = jsonObject.get("name").getAsString();

        final HttpUriRequest deleteChatRoomsRequest = RequestBuilder.delete(CHAT_URL + "/chats/" + id)
                .build();

        try {
            doRequest(deleteChatRoomsRequest);
        } catch (Exception e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }

        final LoginDTO loginDTO = loginAsTestUser();
        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        boolean chatRoomExists = false;

        for(ChatRoomDTO chatRoomDTO: chatRoomDTOs){
            if(newChatName.equals(chatRoomDTO.name)){
                chatRoomExists = true;
            }
        }

        Assert.assertFalse("Error chat room exists after delete.", chatRoomExists);
    }

    private List<ChatRoomDTO> readAllChatRooms(LoginDTO loginDTO) {

        final HttpUriRequest getChatRoomsRequest = RequestBuilder.get(CHAT_URL + "/chats" + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        String chatRoomResponse = "";
        try {
            chatRoomResponse = doRequest(getChatRoomsRequest);
        } catch (Exception e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }

        List<ChatRoomDTO> result = new ArrayList<>();
        final JsonArray jsonArray = new JsonParser().parse(chatRoomResponse).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            final JsonObject jsonObject = jsonElement.getAsJsonObject();

            result.add(new ChatRoomDTO(jsonObject.get("id").getAsLong(), jsonObject.get("name").getAsString()));
        }
        return result;
    }

}
