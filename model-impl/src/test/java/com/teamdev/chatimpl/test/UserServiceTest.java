package com.teamdev.chatimpl.test;

import com.teamdev.chat.dto.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class UserServiceTest extends AbstractTest {

    @Test
    public void testReadUserProfile(){
        final RegisterUserDTO newUser = new RegisterUserDTO("read_user", "pass", new Date());
        userManagementService.register(newUser);
        final LoginDTO user = userAuthenticationService.login("read_user", "pass");

        final UserProfileDTO userProfile = userService.readUserProfile(new UserId(testUser.userId), new UserId(user.userId), new TokenDTO(testUser.token));
        userManagementService.deleteUser(new UserId(user.userId));

        Assert.assertEquals("Wrong user name.", newUser.login, userProfile.name);
        Assert.assertEquals("Wrong user birthday.", newUser.getBirthday(), userProfile.getBirthday());
    }

    @Test
    public void testReadUserProfileFailsOnReadProfileNotExistedUser(){
        try {
            userService.readUserProfile(new UserId(testUser.userId), new UserId(-1), new TokenDTO(testUser.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "No user found with id -1.", e.getMessage());
        }
    }

    @Test
    public void testReadCurrentUserProfile(){
        final UserProfileDTO userProfile = userService.readCurrentUserProfile(new UserId(testUser.userId), new TokenDTO(testUser.token));

        Assert.assertEquals("Wrong user id.", testUserProfile.id, userProfile.id);
        Assert.assertEquals("Wrong user name.", testUserProfile.name, userProfile.name);
        Assert.assertEquals("Wrong user birthday.", testUserProfile.getBirthday(), userProfile.getBirthday());
    }

}
