package com.learningcrew.linkupuser.query.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ViewController {
    @GetMapping("/register/success")
    public String registerSuccess() {
        return "register-success";
    }
}
