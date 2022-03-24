package com.security.example.securityExample.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}