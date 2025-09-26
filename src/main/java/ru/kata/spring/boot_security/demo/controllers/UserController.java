package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.services.UserServ;

import java.security.Principal;


@Controller
public class UserController {

    private final UserServ userServ;

    @Autowired
    public UserController(UserServ userServ) {
        this.userServ = userServ;
    }

    @GetMapping("/user")
    public String show(Model model, Principal principal) {
        model.addAttribute("user", userServ.findByEmail(principal.getName()));
        return "user";
    }
}
