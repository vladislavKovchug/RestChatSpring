package com.teamdev.chat.test.integration;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.request.LoginRequest;
import com.teamdev.chat.request.RegisterUserRequest;
import com.teamdev.chat.test.exception.HttpRequestFailedException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class UserIntegrationTest extends IntegrationTest {

    @Test
    public void testUserRegisterAndDelete(){

        final String newUserPassword = "password";
        final String newUserLogin = "Test_User";
        final HttpUriRequest createUserRequest = addJsonParameters(RequestBuilder.post(CHAT_URL + "/register"),
                new RegisterUserRequest(newUserLogin, newUserPassword, new Date().getTime()))
                .build();

        String response = doRequestWithAssert(createUserRequest);

        final JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        final long id = jsonObject.get("id").getAsLong();
        final String name = jsonObject.get("name").getAsString();

        final UserProfileDTO registeredUser = new UserProfileDTO(id, name, new Date());

        final LoginDTO loginDTO = loginUser(registeredUser.name, newUserPassword);

        final List<ChatRoomDTO> chatRoomDTOs = readAllChatRooms(loginDTO);
        Assert.assertTrue("should be selected at least 2 chat rooms", chatRoomDTOs.size() >= 2);

        final HttpUriRequest leaveChatRoomSecondRequest = RequestBuilder.delete(CHAT_URL + "/users/" + loginDTO.userId)
                .build();
        final String deleteResponse = doRequestWithAssert(leaveChatRoomSecondRequest);

        try{
            final HttpUriRequest loginRequest = addJsonParameters(RequestBuilder.post(LOGIN_CHAT_URL),
                    new LoginRequest(registeredUser.name, newUserPassword)).build();
            doRequest(loginRequest);
            Assert.fail("Error, Logged with deleted user.");
        } catch (HttpRequestFailedException e){
            Assert.assertEquals("Error, wrong status code.", 403, e.getStatusLine().getStatusCode());
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + LOGIN_CHAT_URL + " :" + e.getMessage());
        }

    }



}
