package com.teamdev.chatimpl.test;

import org.junit.Test;

public class UserServiceTest extends AbstractTest {

    @Test
    public void someTest(){

    }

    /*
    private RegisterUserDTO registerUserDTO = new RegisterUserDTO("not_ivan", "123456", 123, new Date(1700, 10, 10));
    private UserProfileDTO testUser;
    private String testUserToken = "";

    private String RegisterAndLoginAsTestUser() {
        userManagementService.register(registerUserDTO);
        final String token = userAuthenticationService.login(registerUserDTO.login, registerUserDTO.password);
        testUser = userService.readCurrentUserProfile(token);

        return token;
    }


    @Before
    public void before(){
        testUserToken = RegisterAndLoginAsTestUser();
    }

    @After
    public void after(){
        userManagementService.deleteUser(testUser.id);
        testUserToken = "";
    }

    @Test
    public void testReadUserProfile(){
        final RegisterUserDTO newUser = new RegisterUserDTO("read_user", "pass", 123, new Date());
        userManagementService.register(newUser);
        final String token = userAuthenticationService.login("read_user", "pass");
        final long userId = userAuthenticationService.readCurrentUserId(token);

        final UserProfileDTO userProfile = userService.readUserProfile(testUserToken, userId);
        userManagementService.deleteUser(userId);

        assertEquals("Wrong user name.", newUser.login, userProfile.name);
        assertEquals("Wrong user age.", new Long(newUser.age), userProfile.age);
        assertEquals("Wrong user birthday.", newUser.getBirthday(), userProfile.getBirthday());
    }

    @Test
    public void testReadUserProfileFailsOnReadProfileNotExistedUser(){
        try {
            userService.readUserProfile(testUserToken, 666);
            fail("Exception should be thrown.");
        } catch (Exception e) {
            assertEquals("Not correct Exception message.", "No user found with id 666.", e.getMessage());
        }
    }

    @Test
    public void testReadCurrentUserProfile(){
        final UserProfileDTO userProfile = userService.readCurrentUserProfile(testUserToken);

        assertEquals("Wrong user name.", registerUserDTO.login, userProfile.name);
        assertEquals("Wrong user age.", new Long(registerUserDTO.age), userProfile.age);
        assertEquals("Wrong user birthday.", registerUserDTO.getBirthday(), userProfile.getBirthday());
    }
*/
}
