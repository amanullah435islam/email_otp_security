package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    public String dashboard() {

        return "Welcome User";
    }
}
















//@RestController
//@RequestMapping("/auth")
//public class UserController {
//
//    @GetMapping("/profile")
//    public String profile() {
//
//        return "Welcome Protected API";
//    }
//}