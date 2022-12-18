package com.osh4.accounting.controller;


import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersPageController {
    private final UserService userService;

    @Autowired
    public UsersPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userListPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @PostMapping("/async")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
