package com.iamdemo.identity_access_governance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String home() {
        return "Identity Access Governance app is running successfully!";
    }
    
}
