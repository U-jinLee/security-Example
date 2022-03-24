package com.security.example.securityExample.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;
}
