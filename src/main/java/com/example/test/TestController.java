package com.example.test;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public String publicApi() {
        return "Public API - No Auth Needed";
    }

    @GetMapping("/user")
    public String userApi() {
        return "USER API - Access Granted";
    }

    @GetMapping("/doctor")
    public String doctorApi() {
        return "DOCTOR API - Access Granted";
    }

    @GetMapping("/admin")
    public String adminApi() {
        return "ADMIN API - Access Granted";
    }
}
