package com.teamdev.chat.test.integration;

import com.google.gson.*;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.request.LoginRequest;
import com.teamdev.chat.test.exception.HttpRequestFailedException;
import org.apache.commons.io.IOUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class IntegrationTest {

    protected static final String CHAT_ROOM_HOST = "localhost:8080";

    protected static final String CHAT_URL = "http://" + CHAT_ROOM_HOST + "/chat";
    protected static final String LOGIN_CHAT_URL = CHAT_URL + "/login";
    protected static final String USER_LOGIN = "user1";
    protected static final String USER_PASSWORD = "12345";

    protected static final String TOKEN_PARAMETER_NAME = "token";

    protected String doRequest(HttpUriRequest request) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new HttpRequestFailedException(response.getStatusLine());
        }

        final InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        final String result = IOUtils.toString(reader);
        reader.close();

        return result;
    }

    protected String doRequestWithAssert(HttpUriRequest request) {
        String result = "";
        try {
            result = doRequest(request);
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + request.getURI().toString() + " :" + e.getMessage());
        }
        return result;
    }

    protected StatusLine doFailRequest(HttpUriRequest request){
        try {
            doRequest(request);
            Assert.fail("Error, expected exception throw.");
        } catch (HttpRequestFailedException e) {
            return e.getStatusLine();
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + CHAT_URL + "/chats" + " :" + e.getMessage());
        }
        return null;
    }

    protected RequestBuilder addJsonParameters(RequestBuilder builder, Object parameters) {
        final Gson gson = new GsonBuilder().create();
        StringEntity params = new StringEntity(gson.toJson(parameters), "UTF-8");
        params.setContentType("application/json; charset=UTF-8");
        return builder.setEntity(params).setHeader("Content-Type", "application/json");
    }

    protected LoginDTO loginAsTestUser() {
        return loginUser(USER_LOGIN, USER_PASSWORD);
    }

    protected LoginDTO loginUser(String login, String password) {
        final HttpUriRequest loginRequest = addJsonParameters(RequestBuilder.post(LOGIN_CHAT_URL),
                new LoginRequest(login, password)).build();

        String loginResponse = doRequestWithAssert(loginRequest);

        JsonObject loginJsonObject = new JsonParser().parse(loginResponse).getAsJsonObject();

        final long userId = loginJsonObject.get("userId").getAsLong();
        final String userToken = loginJsonObject.get("token").getAsString();

        return new LoginDTO(userId, userToken);
    }

    protected List<ChatRoomDTO> readAllChatRooms(LoginDTO loginDTO) {
        final HttpUriRequest getChatRoomsRequest = RequestBuilder.get(CHAT_URL + "/chats/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token)
                .build();

        String chatRoomResponse = doRequestWithAssert(getChatRoomsRequest);

        List<ChatRoomDTO> result = new ArrayList<>();
        final JsonArray jsonArray = new JsonParser().parse(chatRoomResponse).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            final JsonObject jsonObject = jsonElement.getAsJsonObject();
            result.add(new ChatRoomDTO(jsonObject.get("id").getAsLong(), jsonObject.get("name").getAsString()));
        }
        return result;
    }

}
