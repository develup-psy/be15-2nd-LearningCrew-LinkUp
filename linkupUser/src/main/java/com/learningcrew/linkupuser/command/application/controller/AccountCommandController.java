package com.learningcrew.linkupuser.command.application.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class AccountCommandController {
    /* 계좌 등록 */

    /* 사업자 등록 */

    /* 포인트 증가 */
    @PostMapping("/users/{userId}/point/increase")
    void increasePoint(@PathVariable("userId") Long userId, @RequestParam int amount){

    }
}
