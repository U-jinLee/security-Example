package com.security.example.securityExample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터를 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화, preAuthorize 활성화
public class webSecurityConfig extends WebSecurityConfigurerAdapter {

    /*해당 메서드의 리턴되는 오브젝트를 IOC로 등록*/
    @Bean // 비밀번호를 암호화 시켜야 시큐리티 로그인이 가능하다
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated() // `인증만`되면 들어갈 수 있는 주소!
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .anyRequest().permitAll()
                // 권한없는 페이지에 요청이 들어갈 때 로그인 페이지로 옮긴다
                .and()
                .formLogin()
                .loginPage("/loginForm")
                // '/login'이라는 주소가 호출이 되면 시큐리티가 대신 로그인을 진행해준다.(로그인 프로세스를 구현X)
                .loginProcessingUrl("/login")
                // 로그인 성공 시 redirect 주소
                .defaultSuccessUrl("/index");

    }
}
