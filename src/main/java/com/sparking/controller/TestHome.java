package com.sparking.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class TestHome {
    @GetMapping("")
    public String loginGoogle() {
        return String.format("redirect:%s", "http://hongdatchy.me/swagger-ui.html");
    }
}
