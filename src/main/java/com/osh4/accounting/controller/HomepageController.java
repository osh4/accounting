package com.osh4.accounting.controller;

import com.osh4.accounting.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomepageController {
    private final AccountService accountService;

    @Autowired
    public HomepageController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String homepage(Model model) {
        model.addAttribute("accounts", accountService.getAllAccountsByUserCode("testuser1"));
        return "index";
    }


}
