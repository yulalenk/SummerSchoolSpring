package com.example.spring.config;

import com.example.spring.security.SecurityConfig;
import com.example.spring.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


    private final TokenProvider tokenProvider;

    private static final String USER_ENDPOINT = "/users/list";
    private static final String RESULT_ENDPOINT = "/users/result";
    private static final String LOGOUT_ENDPOINT = "/users/logout";
    private static final String EMAIL_ENDPOINT = "/users/email";

    @Autowired
    public WebSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, USER_ENDPOINT).authenticated()
                .antMatchers(HttpMethod.POST, LOGOUT_ENDPOINT).authenticated()
                .antMatchers(HttpMethod.POST, RESULT_ENDPOINT).authenticated()
                .antMatchers(HttpMethod.POST, EMAIL_ENDPOINT).authenticated()
                .anyRequest().permitAll()
                .and()
                .apply(new SecurityConfig(tokenProvider));
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*");
    }
}
