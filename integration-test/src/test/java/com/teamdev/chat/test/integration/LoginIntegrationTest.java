package com.teamdev.chat.test.integration;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.request.LoginRequest;
import com.teamdev.chat.test.exception.HttpRequestException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class LoginIntegrationTest extends IntegrationTest {

    @Test
    public void testUserLogin(){
        final LoginDTO loginDTO = loginAsTestUser();

        final HttpUriRequest getChatRoomsRequest =
                RequestBuilder.get(CHAT_URL + "/users/" + loginDTO.userId + "/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token).build();

        String currentUserProfile = doRequestWithAssert(getChatRoomsRequest);

        final JsonObject jsonObject = new JsonParser().parse(currentUserProfile).getAsJsonObject();
        final String name = jsonObject.get("name").getAsString();

        Assert.assertEquals("Logged in user name does not match with user profile.", USER_LOGIN, name);
    }

    @Test
    public void testUserLoginFailsWithBadCredentials(){
        try{
            final HttpUriRequest loginRequest = addJsonParameters(RequestBuilder.post(LOGIN_CHAT_URL),
                    new LoginRequest("admin", "admin")).build();
            doRequest(loginRequest);
            Assert.fail("Error, expected exception throw.");
        } catch (HttpRequestException e){
            Assert.assertEquals("Error, wrong status code.", 403, e.getStatusLine().getStatusCode());
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + LOGIN_CHAT_URL + " :" + e.getMessage());
        }
    }

    @Test
    public void testUserLogout(){
        final LoginDTO loginDTO = loginAsTestUser();

        final HttpUriRequest logoutRequest =
                RequestBuilder.delete(CHAT_URL + "/logout/" + loginDTO.userId)
                .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token).build();

        doRequestWithAssert(logoutRequest);

        final HttpUriRequest getChatRoomsRequest =
                RequestBuilder.get(CHAT_URL + "/users/" + loginDTO.userId + "/" + loginDTO.userId)
                        .addParameter(TOKEN_PARAMETER_NAME, loginDTO.token).build();

        try{
            doRequest(getChatRoomsRequest);
            Assert.fail("Error, user was logged out, exception should be thrown.");
        } catch (HttpRequestException e){
            Assert.assertEquals("Error, wrong status code.", 403, e.getStatusLine().getStatusCode());
        } catch (IOException e) {
            Assert.fail("Error while request to URL " + LOGIN_CHAT_URL + " :" + e.getMessage());
        }

    }

}
