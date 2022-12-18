package com.osh4.accounting.controller;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.entity.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LoginController {


    public static final String LOGIN_VIEW = "login";

    @GetMapping("/login")
    public String login(Model model) {
//        model.addAttribute("userForm", new User());

        return LOGIN_VIEW;
    }

    @GetMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return LOGIN_VIEW;
    }
//
//    @PostMapping
//    public String login(@ModelAttribute UserDto user, Model model) {
//        return "/test";
//    }
}
