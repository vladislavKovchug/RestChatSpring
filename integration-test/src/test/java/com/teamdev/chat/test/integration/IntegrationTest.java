package com.teamdev.chat.test.integration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class IntegrationTest {

    public static final String LOGIN_CHAT_URL = "http://localhost:8080/chat/login";
    public static final String CHAT_GET_CHAT_ROOM_LIST_URL = "http://localhost:8080/chat/chats";
    public static final String USER_LOGIN = "user1";
    public static final String USER_PASSWORD = "12345";

    public static final String LOGIN_PARAMETER_NAME = "login";
    public static final String PASSWORD_PARAMETER_NAME = "pass";
    public static final String CHAT_ROOM_LIST = "[{\"id\":1,\"name\":\"chat\"},{\"id\":2,\"name\":\"chat 2\"}]";
    public static final String USER_ID_PARAMETER_NAME = "userid";
    public static final String TOKEN_PARAMETER_NAME = "token";

    private String doRequest(HttpUriRequest request) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final CloseableHttpResponse response = httpClient.execute(request);

        if(response.getStatusLine().getStatusCode() != 200){
            throw new RuntimeException(response.getStatusLine().toString());
        }

        final InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        final String result = IOUtils.toString(reader);
        reader.close();

        return result;
    }

    @Test
    public void testReadChatRoomsList(){

        final HttpUriRequest loginRequest = RequestBuilder.get(LOGIN_CHAT_URL)
                .addParameter(LOGIN_PARAMETER_NAME, USER_LOGIN)
                .addParameter(PASSWORD_PARAMETER_NAME, USER_PASSWORD)
                .build();

        String loginResponse = "";
        try {
            loginResponse = doRequest(loginRequest);
        } catch (Exception e) {
            Assert.fail("Error while request to URL " + LOGIN_CHAT_URL + " :" + e.getMessage());
        }

        JsonObject loginJsonObject = new JsonParser().parse(loginResponse).getAsJsonObject();
        final String userId = loginJsonObject.get("userId").toString();
        final String userToken = loginJsonObject.get("token").getAsString();

        final HttpUriRequest getChatRoomsRequest = RequestBuilder.get(CHAT_GET_CHAT_ROOM_LIST_URL)
                .addParameter(USER_ID_PARAMETER_NAME, userId)
                .addParameter(TOKEN_PARAMETER_NAME, userToken)
                .build();
        String chatRoomResponse = "";
        try {
            chatRoomResponse = doRequest(getChatRoomsRequest);
        } catch (Exception e) {
            Assert.fail("Error while request to URL " + CHAT_GET_CHAT_ROOM_LIST_URL + " :" + e.getMessage());
        }

        Assert.assertEquals("Wrong chat rooms", CHAT_ROOM_LIST, chatRoomResponse);
    }

    @Test
    public void testReadChatRoomsListFailsOnInvalidToken() throws IOException {
        final HttpUriRequest getChatRoomsRequest = RequestBuilder.get(CHAT_GET_CHAT_ROOM_LIST_URL)
                .addParameter(USER_ID_PARAMETER_NAME, "1")
                .addParameter(TOKEN_PARAMETER_NAME, "some-token")
                .build();
        try {
            doRequest(getChatRoomsRequest);
            Assert.fail("Exception should be thrown.");
        } catch (RuntimeException e) {
            Assert.assertEquals("Wrong Error code.", "HTTP/1.1 403 Forbidden", e.getMessage());
        }
    }

}
