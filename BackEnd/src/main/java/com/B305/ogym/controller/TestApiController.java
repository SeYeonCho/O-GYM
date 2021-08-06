package com.B305.ogym.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/test")
public class TestApiController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}

