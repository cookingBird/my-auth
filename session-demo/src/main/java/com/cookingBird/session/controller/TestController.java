package com.cookingBird.session.controller;

import com.cookingBird.session.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/session")
public class TestController {
    public static final List<User> users = new LinkedList<User>() {
        {
            push(User.builder()
                    .name("张三")
                    .account("123")
                    .password("456")
                    .permission(new String[]{"admin"})
                    .build());
            push(User.builder()
                    .name("李四")
                    .account("456")
                    .password("789")
                    .permission(new String[]{"admin"})
                    .build());
        }
    };


    @GetMapping("/hello-world")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/login")
    public boolean login() {
        return false;
    }

    @GetMapping("/info")
    public String getInfo() {
        return "hello world";
    }
}
