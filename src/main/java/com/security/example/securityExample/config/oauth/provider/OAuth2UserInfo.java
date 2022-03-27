package com.security.example.securityExample.config.oauth.provider;

import lombok.Getter;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
