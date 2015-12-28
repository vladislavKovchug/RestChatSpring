package com.teamdev.chatimpl.test;

import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.UserProfileDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserAuthenticationServiceTest extends AbstractTest {

    @Test
    public void testUserLogin(){
        userManagementService.register(new RegisterUserDTO("user1", "12345", 12, new Date()));
        final String token = userAuthenticationService.login("user1", "12345");
        String passwordHash = "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5";

        chatRoomService.readAllChatRooms(token);
    }

    @Test
    public void testFailsOnUserLoginWithBadCredential() {
        try{
            final String token = userAuthenticationService.login("admin", "admin");
            fail("Exception should be thrown.");
        } catch (Exception e){
            assertEquals("Not correct Exception message.", "Access denied.", e.getMessage());
        }
    }

    @Test
    public void testReadCurrentUserId(){
        final String token = userAuthenticationService.login("user1", "12345");
        final long userId = userAuthenticationService.readCurrentUserId(token);
        final UserProfileDTO userProfileDTO = userService.readUserProfile(token, userId);
        Assert.assertEquals("Wrong readed userId from token. names should be equals.", "user1",userProfileDTO.name);
    }

    @Test
    public void testFailsOnReadCurrentUserIdWithBadToken() {
        try{
            userAuthenticationService.readCurrentUserId("1-123-123");
            fail("Exception should be thrown.");
        } catch (Exception e){
            assertEquals("Not correct Exception message.", "Access denied.", e.getMessage());
        }
    }

}
