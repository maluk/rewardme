package com.rewardme.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("test")
public class TestController {
    @GetMapping("/test")
    public String test(Model model) {
        return "test";
    }

    @GetMapping("/post")
    public String post(Model model) {
        return "post";
    }

    @GetMapping("/post2")
    public String post2(Model model) {
        return "post2";
    }
}
