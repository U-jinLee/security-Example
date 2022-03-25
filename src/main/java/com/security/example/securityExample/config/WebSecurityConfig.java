package com.security.example.securityExample.config;

/**
 * OAuth2
 * 1. 코드받기(인증), 2.엑세스토큰(권한)
 * 3. 사용자 프로필 정보를 가져오고
 * 4-1. 그 정보를 토대로 회원가입을 자동으로 진행 4-2. 정보가 모자라면 추가적인 구성 정보를 요구
 * */

import com.security.example.securityExample.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration // 자바파일을 IOC로 등록하기 위해서 Cofiguration 등록
@EnableWebSecurity // 스프링 시큐리티 필터를 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화, preAuthorize 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    /*해당 메서드의 리턴되는 오브젝트를 IOC로 등록*/
    @Bean // 비밀번호를 암호화 시켜야 시큐리티 로그인이 가능하다
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                .defaultSuccessUrl("/index")
                .and()
                //구글 로그인이 완료된 후의 후처리가 필요 -> 코드(X), 엑세스토큰 + 사용자프로필정보
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                ;

    }
}