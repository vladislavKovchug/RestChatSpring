package com.teamdev.chat.test;

import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserManagementService;
import com.teamdev.chatimpl.service.UserAuthenticationServiceImpl;
import com.teamdev.database.ChatDatabase;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public abstract class AbstractTest {

    @Configuration
    @ComponentScan("com.teamdev.chatimpl")
    static class ContextConfiguration {

        @Bean
        public ChatDatabase chatDatabase() {
            return new ChatDatabase();
        }
    }

    @Inject
    UserAuthenticationService userAuthenticationService;

    @Inject
    ChatRoomService chatRoomService;

    @Inject
    UserManagementService userManagementService;

}
