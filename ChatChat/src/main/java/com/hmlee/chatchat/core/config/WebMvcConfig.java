package com.hmlee.chatchat.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web MVC configuration class
 * - Spring MVC 관련 설정을 다룬다.
 * - Spring의 기존 dispatcher-servlet.xml의 역할을 대신한다.
 *
 * Created by hmlee
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    /**
     * external-config.properties 파일로부터 properties을 load한다.
     *
     * <code>@Value</code>, ${...}를 사용하여 properties를 참조하기 위해 필요하다.
     * {@link Application}에서 {@link PropertySourcesPlaceholderConfigurer}를 설정하였다 하더라도,
     * Controller에서 properties를 참조하려면 추가적으로 설정해야 한다.
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        logger.debug("★★★★★ Setting up property source at WebMvcConfig ★★★★★");
        PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[] {
                new ClassPathResource("/config/external-config.properties")
        };
        propertySources.setLocations(resources);
        propertySources.setFileEncoding("UTF-8");
        propertySources.setIgnoreUnresolvablePlaceholders(true);
        return propertySources;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
