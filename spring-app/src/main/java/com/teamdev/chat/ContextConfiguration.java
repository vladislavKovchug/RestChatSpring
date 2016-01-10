package com.teamdev.chat;

import com.teamdev.chat.proxy.AuthenticationCheckerAspect;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.Tables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;


@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.teamdev.chat.impl")
public class ContextConfiguration {

    @Bean
    public ChatDatabase chatDatabase(){
        final ChatDatabase chatDatabase = new ChatDatabase();
        chatDatabase.createTable(Tables.USERS_TABLE);
        chatDatabase.createTable(Tables.TOKENS_TABLE);
        chatDatabase.createTable(Tables.MESSAGES_TABLE);
        chatDatabase.createTable(Tables.CHAT_ROOMS_TABLE);
        return chatDatabase;
    }

    @Bean AuthenticationCheckerAspect authenticationCheckerAspect(){
        return new AuthenticationCheckerAspect();
    }

    @PostConstruct
    public void onContextInitialize(){
        System.out.println("spring context was initialized");
    }

}
