package com.teamdev.chat.test.integration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.test.exception.HttpRequestException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class IntegrationTest {

    protected static final String CHAT_ROOM_HOST = "localhost:8080";

    protected static final String CHAT_URL = "http://" + CHAT_ROOM_HOST + "/chat";
    protected static final String LOGIN_CHAT_URL = CHAT_URL + "/login";
    protected static final String USER_LOGIN = "user1";
    protected static final String USER_PASSWORD = "12345";

    protected static final String LOGIN_PARAMETER_NAME = "login";
    protected static final String PASSWORD_PARAMETER_NAME = "password";
    protected static final String USER_ID_PARAMETER_NAME = "userid";
    protected static final String TOKEN_PARAMETER_NAME = "token";

    protected String doRequest(HttpUriRequest request) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final CloseableHttpResponse response = httpClient.execute(request);

        if(response.getStatusLine().getStatusCode() != 200){
            throw new HttpRequestException(response.getStatusLine());
        }

        final InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        final String result = IOUtils.toString(reader);
        reader.close();

        return result;
    }

    protected LoginDTO loginAsTestUser(){

        final Gson gson = new Gson();
        final JsonObject jsonElement = new JsonObject();
        jsonElement.addProperty(LOGIN_PARAMETER_NAME, USER_LOGIN);
        jsonElement.addProperty(PASSWORD_PARAMETER_NAME, USER_PASSWORD);

        StringEntity params = new StringEntity(gson.toJson(jsonElement), "UTF-8");
        //params.setContentType("application/json; charset=UTF-8");

        HttpPost post = new HttpPost(LOGIN_CHAT_URL);
        post.setEntity(params);
        post.addHeader("Content-Type", "application/json");

        final HttpUriRequest loginRequest = RequestBuilder.post(LOGIN_CHAT_URL)
                .setEntity(params)
                .setHeader("Content-Type", "application/json")
                .build();


        String loginResponse = "";
        try {
            loginResponse = doRequest(post);
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + LOGIN_CHAT_URL + " :" + e.getMessage());
        }

        JsonObject loginJsonObject = new JsonParser().parse(loginResponse).getAsJsonObject();

        final long userId = loginJsonObject.get("userId").getAsLong();
        final String userToken = loginJsonObject.get("token").getAsString();

        return new LoginDTO(userId, userToken);
    }

}
