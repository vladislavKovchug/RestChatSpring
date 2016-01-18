package com.teamdev.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamdev.database.ChatDatabase;
import com.teamdev.database.Tables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;


@EnableWebMvc
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"com.teamdev.chat.impl", "com.teamdev.chat.proxy", "com.teamdev.chat.controller"})
public class ContextConfiguration extends WebMvcConfigurerAdapter {

    @Inject
    SampleDataCreator sampleDataCreator;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }


    @Bean
    public ChatDatabase chatDatabase() {
        final ChatDatabase chatDatabase = new ChatDatabase();
        chatDatabase.createTable(Tables.USERS_TABLE);
        chatDatabase.createTable(Tables.TOKENS_TABLE);
        chatDatabase.createTable(Tables.MESSAGES_TABLE);
        chatDatabase.createTable(Tables.CHAT_ROOMS_TABLE);
        return chatDatabase;
    }

    @Bean
    SampleDataCreator sampleDataCreator() {
        return new SampleDataCreator();
    }

    @PostConstruct
    public void onContextInitialize() {
        sampleDataCreator.createSampleData();
        System.out.println("spring context was initialized");
    }

}
