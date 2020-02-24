package com.eurowings.subscriber.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
@RequestMapping(value = "/")
public class IndexController {

    @GetMapping()
    public String sayHello() {
        return "Hello and Welcome to the Subscriber API.";
    }
}

