package com.security.example.securityExample.config.oauth;

import com.security.example.securityExample.config.auth.PrincipalDetails;
import com.security.example.securityExample.web.domain.User;
import com.security.example.securityExample.web.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    /*구글로부터 받은 userRequest 데이터에 대한 후처리는 이곳에서 이뤄진다.*/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info(String.valueOf(userRequest));
        log.info(String.valueOf(userRequest.getClientRegistration())); //registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info(String.valueOf(userRequest.getAccessToken()));

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(Oauth-client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원프로필을 받아야함(loadUser 함수) -> 구글로부터 회원프로필을 받아준다.
        log.info(String.valueOf(oAuth2User.getAttributes()));

        String provider = userRequest.getClientRegistration().getRegistrationId(); // ex) google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; // ex) google_12342dda...
        String email = oAuth2User.getAttribute("email");
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
        //Autentication 객체 안으로 들어가자
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
