package com.teamdev.chat;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.teamdev.database.entity.ChatRoom;
import com.teamdev.database.entity.User;
import com.teamdev.database.ChatDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;


@Configuration
@ComponentScan("com.teamdev.chatimpl")
public class ContextConfiguration {

    @Bean
    public ChatDatabase chatDatabase(){
        return new ChatDatabase();
    }

    @PostConstruct
    public void onContextInitialize(){
        System.out.println("spring context was initialized");
    }

}
