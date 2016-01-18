package com.teamdev.chat.test;

import com.teamdev.chat.dto.*;
import com.teamdev.chat.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = com.teamdev.chat.test.SpringConfig.class, loader = AnnotationConfigContextLoader.class)
public abstract class AbstractTest {

    @Inject
    protected UserAuthenticationService userAuthenticationService;

    @Inject
    protected ChatRoomService chatRoomService;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected UserService userService;

    @Inject
    protected MessageService messageService;

    protected RegisterUserDTO registerUserDTO = new RegisterUserDTO("ivan", "123456", new Date(1700, 10, 10));
    protected UserProfileDTO testUserProfile;
    protected LoginDTO testUser;
    protected ChatRoomDTO testChatRoom;

    @Before
    public void before() {
        testUser = registerAndLoginAsTestUser();
        testChatRoom = chatRoomService.addChatRoom("test_chat_room");
    }

    @After
    public void after() {
        userManagementService.deleteUser(new UserId(testUser.userId));
        chatRoomService.deleteChatRoom(new ChatRoomId(testChatRoom.id));
        testUser = null;
        testChatRoom = null;
    }

    private LoginDTO registerAndLoginAsTestUser() {
        userManagementService.register(registerUserDTO);
        final LoginDTO token = userAuthenticationService.login(registerUserDTO.login, registerUserDTO.password);
        testUserProfile = userService.readUserProfile(new UserId(token.userId), new UserId(token.userId), new TokenDTO(token.token));

        return token;
    }

}
