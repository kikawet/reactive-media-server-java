package com.example.http2.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping
    public String defaultRequest() {
        return "Hello world";
    }

}
