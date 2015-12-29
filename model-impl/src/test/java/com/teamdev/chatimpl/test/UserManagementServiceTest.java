package com.teamdev.chatimpl.test;


public class UserManagementServiceTest extends AbstractTest {
/*
    @Test
    public void testUserRegister(){
        final RegisterUserDTO newUser = new RegisterUserDTO("new_user", "12345", 12, new Date(1900, 10, 11));
        userManagementService.register(newUser);
        final String newUserToken = userAuthenticationService.login(newUser.login, newUser.password);
        final UserProfileDTO userProfile = userService.readCurrentUserProfile(newUserToken);

        assertEquals("User name changed after register.", newUser.login, userProfile.name);
        assertEquals("User age changed after register.", new Long(newUser.age), userProfile.age);
        assertEquals("User birthday changed after register.", newUser.getBirthday(), userProfile.getBirthday());
    }

    @Test
    public void testUserRegisterFailOnRegisterExistedUser(){
        try {
            final RegisterUserDTO newUser = new RegisterUserDTO("user1", "12345", 12, new Date(1900, 10, 11));
            userManagementService.register(newUser);
            userManagementService.register(newUser);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "User user1 already exists.", e.getMessage());
        }
    }

    @Test
    public void testUserDelete(){
        final RegisterUserDTO newUser = new RegisterUserDTO("user_for_delete", "12345", 12, new Date(1900, 10, 11));
        userManagementService.register(newUser);
        final String token = userAuthenticationService.login(newUser.login, newUser.password);
        final long userId = userAuthenticationService.readCurrentUserId(token);
        userManagementService.deleteUser(userId);

        try{
            userAuthenticationService.readCurrentUserId(token);
            fail("Exception should be thrown.");
        } catch (Exception e){
            assertEquals("Not correct Exception message.", "Access denied.", e.getMessage());
        }
    }

    @Test
    public void testUserDeleteFailOnDeleteNotExistedUser(){
        try {
            userManagementService.deleteUser(666);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "User with id 666 does not exists.", e.getMessage());
        }
    }
*/
}
