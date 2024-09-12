package com.example.oauthjwt2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class q {

    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
