package com.teamdev.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;


@EnableWebMvc
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"com.teamdev.chat", "com.teamdev.chat.proxy", "com.teamdev.chat.controller"})
@EnableJpaRepositories("com.teamdev.chat.repository")
@EnableTransactionManagement
public class ContextConfiguration extends WebMvcConfigurerAdapter {

    @Inject
    private SampleDataCreator sampleDataCreator;

    private static final Logger LOGGER = Logger.getLogger(ContextConfiguration.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.teamdev.chat.entity");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/chat");
        dataSource.setUsername( "root" );
        dataSource.setPassword( "toor" );
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return properties;
    }

    @Bean
    SampleDataCreator sampleDataCreator() {
        return new SampleDataCreator();
    }

    @PostConstruct
    public void onContextInitialize() {
        sampleDataCreator.createSampleData();
        LOGGER.trace("context was initialized");
    }

}
