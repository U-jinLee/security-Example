package com.security.example.securityExample.config.auth;

import com.security.example.securityExample.web.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행
 * 로그인 진행이 완료 -> Security Session 생성 -> Security ContextHolder에 세션정보 저장
 * 오브젝트 타입 => Authentication 객체만이 Security ContextHolder에 저장 가능하다. -> Authentication 정보 안에 유저정보가 있어야 함
 * User 오브젝트 타입 => UserDetails 타입 객체
 * Security Session => Authentication => UserDetails
 * */


@Getter
public class PrincipalDetails implements UserDetails, OAuth2User /*Authentication 객체는 UserDetails, OAuth2User로 다운캐스팅 가능하므로 두 가지를 Impl화 시켜준다*/ {
    private User user;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth2 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //해당 유저의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    /*OAuth2User의 메서드*/
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
    /***/
}
