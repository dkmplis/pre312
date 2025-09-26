package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.services.Services;

import java.security.Principal;


@Controller
public class UserController {

    private final Services userServ;

    @Autowired
    public UserController(Services userServ) {
        this.userServ = userServ;
    }

    @GetMapping("/user")
    public String show(Model model, Principal principal) {
        model.addAttribute("user", userServ.findByUserName(principal.getName()));
        return "user";
    }
}
