package com.hmlee.chatchat.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.hmlee.chatchat.core.constant.AccountRole;

import javax.servlet.Filter;


/**
 * Web Security Configuration Class
 * - Spring Security의 설정을 다루는 클래스
 *
 * Created by hmlee
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    protected UserDetailsService userDetailsService;

    @Bean
    public SessionRegistryImpl sessionRegistry() {
        logger.debug("Setting up SessionRegistry");
        return new SessionRegistryImpl();
    }

    /**
     * Configure HTTP Security
     *
     * HTTP 요청에 대한 다음과 같은 내용을 설정한다.
     * - 권한별 URL 접근 제어
     * - 로그인 페이지 지정
     * - 세션 설정
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/webjars/**", "/api/**", "/cms/login", "/favicon.ico", "/home", "/console/**").permitAll()
                .antMatchers("/cms/account").hasAnyAuthority(AccountRole.ROLE_ADMIN.getRoleCode())
                .antMatchers(
                        "/dashboard",
                        "/statistics").hasAnyAuthority(AccountRole.ROLE_ADMIN.getRoleCode(), AccountRole.ROLE_OPERATOR.getRoleCode(), AccountRole.ROLE_AGENT.getRoleCode())
                .anyRequest().fullyAuthenticated()
                .and()
            .formLogin()
                .loginPage("/cms/login")
                .failureUrl("/cms/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/cms/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) // 로그아웃 후 HttpSession을 무효화 시키는 설정
                .deleteCookies("JSESSIONID") // 로그아웃 시 세션에서 사용된 쿠키를 삭제하도록 하는 설정
                .permitAll();
        //http.headers().frameOptions().disable();
    }

    /**
     * Configure Web Security
     *
     * static 디렉토리 하위 항목에 대한 접근제어를 설정한다.
     * - 예 css, js 디렉토리 등
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/style.css", "/i18n/**");
    }

    /**
     * 인증 설정
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 인증 성공 시 처리 Bean
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    /**
     * Spring Security Filter Bean
     *
     * @param securityFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean securityFilterChain(@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) Filter securityFilter) {
        logger.debug("★★★★★ Add Spring Security Filter ★★★★★");
        FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
        registration.setMatchAfter(true);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
