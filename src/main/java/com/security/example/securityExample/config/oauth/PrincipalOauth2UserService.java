package com.security.example.securityExample.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    /*구글로부터 받은 userRequest 데이터에 대한 후처리는 이곳에서 이뤄진다.*/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info(String.valueOf(userRequest));
        log.info(String.valueOf(userRequest.getClientRegistration())); //registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info(String.valueOf(userRequest.getAccessToken()));
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(Oauth-client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원프로필을 받아야함(loadUser 함수) -> 구글로부터 회원프로필을 받아준다.
        log.info(String.valueOf(super.loadUser(userRequest).getAttributes()));

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
