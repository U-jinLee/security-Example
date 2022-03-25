package com.security.example.securityExample.web.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;

    // ex) google
    @Column
    private String provider;

    // ex) sub=105741854823815602981
    @Column
    private String providerId;

//    @Builder
//    public User(String username, String email, String password, String role) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }

    @Builder//(builderMethodName = "OAuth2Builder")
    public User(String username, String email, String password, String role, String provider, String providerId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}