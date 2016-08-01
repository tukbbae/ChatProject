package com.hmlee.chatchat.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 이메일 발송을 위한 설정 Class
 *
 * Created by hmlee
 */
@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender getJavaMailSenderImpl(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        Properties props = new Properties();

        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.transport.protocol", "smtp");

        javaMailSender.setJavaMailProperties(props);
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("tukbbae@gmail.com");
        javaMailSender.setPassword("1234");

        return javaMailSender;
    }

}
