package com.teamdev.chatimpl.test;

import com.teamdev.chat.service.*;
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
    @ComponentScan("com.teamdev.chat.impl")
    static class ContextConfiguration {

        @Bean
        public ChatDatabase chatDatabase() {
            return new ChatDatabase();
        }
    }

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

}
