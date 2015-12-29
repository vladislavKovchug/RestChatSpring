package com.teamdev.servlet;


import com.teamdev.chat.ContextConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextContainer {
    private static ApplicationContext applicationContext;

    public void init(){
        applicationContext = new AnnotationConfigApplicationContext(ContextConfiguration.class);
    }

    public ApplicationContext getApplicationContext(){
        if(applicationContext == null){
            throw new RuntimeException("Application context not initialized");
        }
        return applicationContext;
    }

}
