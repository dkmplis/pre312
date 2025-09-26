package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserServ;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserServ userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServ userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUserPage(Model model) {
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("user", new User());
        return "admin/new";
    }

    @PostMapping("/new")
    public String addNewUserByAnAdmin(@ModelAttribute("user") @Valid User user,
                             @RequestParam(value = "rolesId") List<Integer> rolesId,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/new";
        }
        User newUser = userService.addRoles(user, rolesId);
        userService.addNew(newUser);
        return "redirect:/admin/";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") int id) {
        userService.deleteById(id);
        return "redirect:/admin/";
    }

    @GetMapping("/edit")
    public String editUser(Model model,
                           @RequestParam(value = "id") int id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.getAll());
        return "admin/edit";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute @Valid User user,
                         @RequestParam(value = "id") int id,
                         @RequestParam(required = false, value = "rolesId") List<Integer> rolesId,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        User updatesUser = userService.addRoles(user, rolesId);
        userService.update(id, updatesUser);
        return "redirect:/admin/";
    }



}
