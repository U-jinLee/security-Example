package com.security.example.securityExample.web.api;

import com.security.example.securityExample.config.auth.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityConfigApi {
    @GetMapping("/manager")
    public String responseManager() {
        return "manager";
    }

    @GetMapping("/admin")
    public String responseAdmin() {
        return "admin";
    }

    @GetMapping("/user")
    public String responseUser() {
        return "user";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public
    String info() { return "private info"; }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public
    String data() { return "data"; }

    @GetMapping("/test/login")
    public String testLogin(Authentication authentication, /*로그인 정보를 가져오는 방법*/
                            @AuthenticationPrincipal PrincipalDetails/*or UserDetails*/ userDetails) {

        log.info((String) authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication: " + principalDetails.getUser().getUsername());
        log.info("userDetailsPrincipal: " + userDetails.getUsername());

        return "세션정보 확인";
    }

    @GetMapping("/test/oauth/login")
    public String testOAuthLogin(Authentication authentication,
                                 @AuthenticationPrincipal OAuth2User oAuth) {

        log.info(String.valueOf(authentication.getPrincipal()));
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication: " + oAuth2User.getAttributes());
        log.info("oAuth: " + oAuth.getAttributes());
        return "oAuth 세션정보 확인";
    }
}