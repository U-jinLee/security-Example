package com.security.example.securityExample.web.controller;

import com.security.example.securityExample.web.DTO.UserDTO;
import com.security.example.securityExample.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthenticateController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/join")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(UserDTO userDTO) {
        log.info("connect"+userDTO.getPassword()+""+userDTO.getEmail()+""+userDTO.getUsername());
        log.info("encodePw====================>"+bCryptPasswordEncoder.encode(userDTO.getPassword()));

        userService.join(userDTO);

        return "redirect:/loginForm";
    }

    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

}
