package com.security.example.securityExample.web.service;

import com.security.example.securityExample.web.DTO.UserDTO;
import com.security.example.securityExample.web.domain.User;
import com.security.example.securityExample.web.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long join(UserDTO userDTO) {
        return userRepository.save(
                User.builder()
                        .username(userDTO.getUsername())
                        .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                        .email(userDTO.getEmail())
                        .role("ROLE_USER")
                        .build()
        ).getId();
    }

}
