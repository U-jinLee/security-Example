package com.security.example.securityExample.config.oauth;

import com.security.example.securityExample.config.auth.PrincipalDetails;
import com.security.example.securityExample.config.oauth.provider.*;
import com.security.example.securityExample.web.domain.User;
import com.security.example.securityExample.web.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    /*구글로부터 받은 userRequest 데이터에 대한 후처리는 이곳에서 이뤄진다.*/
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션 생성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info(String.valueOf(userRequest));
        log.info(String.valueOf(userRequest.getClientRegistration())); //registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info(String.valueOf(userRequest.getAccessToken()));

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(Oauth-client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원프로필을 받아야함(loadUser 함수) -> 구글로부터 회원프로필을 받아준다.
        log.info(String.valueOf(oAuth2User.getAttributes()));

        /*Provider을 이용한 Provider Info 분기 처리*/
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("google login================");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            log.info("facebook login==============");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("naver login==============");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("Kakao login=============="+oAuth2User.getAttributes());
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes());

        }else {
            log.error("지원하지 않는 로그인 방법");
        }
        /**/
        String provider = oAuth2UserInfo.getProvider(); //수정됨: userRequest.getClientRegistration().getRegistrationId(); // ex) google
        String providerId = oAuth2UserInfo.getProviderId(); //oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; // ex) google_12342dda...
        String email = oAuth2UserInfo.getEmail(); //수정됨: oAuth2User.getAttribute("email");
        String password = username+"_"+email;
        String role = "ROLE_USER";


        User user = userRepository.findByUsername(username);
        if(user == null){
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }

        //리턴되는 이 객체는 Autentication 객체 안으로 들어간다.
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
