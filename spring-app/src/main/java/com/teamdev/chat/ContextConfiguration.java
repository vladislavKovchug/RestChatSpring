package com.teamdev.chat;

import com.teamdev.database.ChatDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
@ComponentScan("com.teamdev.chat.impl")
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
