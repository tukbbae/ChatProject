package com.hmlee.chatchat.core.config;

import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Application Class
 * - Web Application의 Entry point
 * - 기존 Spring의 applicationContext.xml의 역할을 대체한다.
 *
 * Created by hmlee
 */
@SpringBootApplication
@EntityScan(basePackages="com.hmlee.chatchat.model.domain")
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class,
        basePackages="com.hmlee.chatchat.repository")
@ComponentScan({
        "com.hmlee.chatchat.controller",
        "com.hmlee.chatchat.service",
        "com.hmlee.chatchat.repository"
})
@Import({WebMvcConfig.class, WebSecurityConfig.class, EmailConfig.class})
public class Application extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Entry-Point
     *
     * @param args
     */
    public static void main(String[] args) {
        logger.info("Welcome ChatChat Web Application!!");
        SpringApplication.run(Application.class, args);
        logger.info("ChatChat Web Application Started!!");
    }

    /**
     * WAR 파일 배포를 위한 설정
     *
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /**
     * external-config.properties 파일로부터 properties을 load한다.
     *
     * <code>@Value</code>, ${...}를 사용하여 properties를 참조하기 위해 필요하다.
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        logger.debug("★★★★★ Setting up property source at Application ★★★★★");
        PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[] {
                new ClassPathResource("/config/external-config.properties")
        };
        propertySources.setLocations(resources);
        propertySources.setFileEncoding("UTF-8");
        propertySources.setIgnoreUnresolvablePlaceholders(true);
        return propertySources;
    }

    /**
     * UTF-8 Character Encoding Filter 추가
     * 
     * @return
     */
    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistration() {
        logger.debug("★★★★★ Add UTF-8 Character Encoding Filter ★★★★★");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }
    
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;

    }

}
