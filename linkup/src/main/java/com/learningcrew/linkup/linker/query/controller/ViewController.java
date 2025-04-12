package com.learningcrew.linkup.linker.query.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ViewController {
    @GetMapping("/register/success")
    public String registerSuccess() {
        return "register-success";
    }
}
