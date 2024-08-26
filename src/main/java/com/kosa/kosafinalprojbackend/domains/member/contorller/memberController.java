package com.kosa.kosafinalprojbackend.domains.member.contorller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class memberController {

    // test controller 개발하면 지워주세요
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
