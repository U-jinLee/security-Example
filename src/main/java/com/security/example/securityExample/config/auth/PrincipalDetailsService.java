package com.security.example.securityExample.config.auth;

import com.security.example.securityExample.web.domain.User;
import com.security.example.securityExample.web.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Authentication 객체를 만드는 역할을 함
 * 시큐리티 설정에서 loginProcessingUrl("/login")
 * login 요청이 들어오면 자동으로 UserDetailsService 타입으로 IOC돼 있는 loadUserByUsername 함수 실행
 * */

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    // 시큐리티 Session(내부 Athentication(내부 userDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) return new PrincipalDetails(userEntity);
        return null;
    }
}
