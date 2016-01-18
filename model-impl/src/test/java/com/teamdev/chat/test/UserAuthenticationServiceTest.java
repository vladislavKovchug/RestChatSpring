package com.teamdev.chat.test;

import com.teamdev.chat.dto.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class UserAuthenticationServiceTest extends AbstractTest {

    @Test
    public void testUserLogin(){
        userManagementService.register(new RegisterUserDTO("user1", "12345", new Date()));
        final LoginDTO user = userAuthenticationService.login("user1", "12345");

        chatRoomService.readAllChatRooms(new UserId(user.userId), new TokenDTO(user.token));
    }

    @Test
    public void testFailsOnUserLoginWithBadCredential() {
        try{
            final LoginDTO user = userAuthenticationService.login("admin", "admin");
            Assert.fail("Exception should be thrown.");
        } catch (Exception e){
            Assert.assertEquals("Not correct Exception message.", "Login Failed.", e.getMessage());
        }
    }

    @Test
    public void testValidateToken(){
        userAuthenticationService.validateToken(new UserId(testUser.userId), new TokenDTO(testUser.token));
    }

    @Test
    public void testValidateTokenFailsWithIncorrectToken(){
        try {
            userAuthenticationService.validateToken(new UserId(testUser.userId), new TokenDTO("incorrect-token"));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Wrong error message", "Access denied.", e.getMessage());
        }
    }

    @Test
    public void testUserLogout(){
        userManagementService.register(new RegisterUserDTO("user2", "12345", new Date()));
        final LoginDTO user = userAuthenticationService.login("user2", "12345");
        userAuthenticationService.logout(new UserId(user.userId), new TokenDTO(user.token));


        try {
            userAuthenticationService.validateToken(new UserId(user.userId), new TokenDTO(user.token));
            Assert.fail("Logged out user is valid.");
        } catch (Exception e) {
            Assert.assertEquals("Wrong error message", "Access denied.", e.getMessage());
        }
    }

}
