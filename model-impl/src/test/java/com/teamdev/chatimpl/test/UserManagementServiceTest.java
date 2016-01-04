package com.teamdev.chatimpl.test;


import com.teamdev.chat.dto.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class UserManagementServiceTest extends AbstractTest {

    @Test
    public void testUserRegister(){
        final RegisterUserDTO newUser = new RegisterUserDTO("new_user", "12345", new Date(1900, 10, 11));
        userManagementService.register(newUser);
        final LoginDTO user = userAuthenticationService.login(newUser.login, newUser.password);
        final UserProfileDTO userProfile = userService.readCurrentUserProfile(new UserId(user.userId), new TokenDTO(user.token));

        Assert.assertEquals("User name changed after register.", newUser.login, userProfile.name);
        Assert.assertEquals("User birthday changed after register.", newUser.getBirthday(), userProfile.getBirthday());
    }

    @Test
    public void testUserRegisterFailOnRegisterExistedUser(){
        try {
            final RegisterUserDTO newUser = new RegisterUserDTO("ivan", "12345", new Date(1900, 10, 11));
            userManagementService.register(newUser);
            userManagementService.register(newUser);
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "User ivan already exists.", e.getMessage());
        }
    }

    @Test
    public void testUserDelete(){
        final RegisterUserDTO newUser = new RegisterUserDTO("user_for_delete", "12345", new Date(1900, 10, 11));
        userManagementService.register(newUser);
        final LoginDTO user = userAuthenticationService.login(newUser.login, newUser.password);
        userManagementService.deleteUser(new UserId(user.userId));

        try{
            userAuthenticationService.validateToken(new UserId(user.userId), new TokenDTO(user.token));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e){
            Assert. assertEquals("Not correct Exception message.", "Access denied.", e.getMessage());
        }
    }

    @Test
    public void testUserDeleteFailOnDeleteNotExistedUser(){
        try {
            userManagementService.deleteUser(new UserId(-1));
            Assert.fail("Exception should be thrown.");
        } catch (Exception e) {
            Assert.assertEquals("Not correct Exception message.", "User with id -1 does not exists.", e.getMessage());
        }
    }

}
