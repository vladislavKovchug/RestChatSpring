package com.teamdev.chat;

import com.teamdev.chat.proxy.AuthenticationCheckerAspect;
import com.teamdev.database.ChatDatabase;
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
        return new ChatDatabase();
    }

    @Bean AuthenticationCheckerAspect authenticationCheckerAspect(){
        return new AuthenticationCheckerAspect();
    }

    @PostConstruct
    public void onContextInitialize(){
        System.out.println("spring context was initialized");
    }

}
