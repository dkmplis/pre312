package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUserPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAll());
        return "admin/new";
    }

    @PostMapping("/new")
    public String addNewUserByAnAdmin(@Valid @ModelAttribute("user") User user,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "rolesId", required = false) List<Integer> rolesId,
                                      Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAll());
            return "admin/new";
        }
        User newUser = userService.addRoles(user, rolesId);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
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
    public String update(@Valid @ModelAttribute User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "rolesId") List<Integer> rolesId,
                         Model model) {
        userValidator.validateForEdit(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAll());
            return "admin/edit";
        }
        User updatesUser = userService.addRoles(user, rolesId);
        updatesUser.setPassword(passwordEncoder.encode(updatesUser.getPassword()));
        userService.update(user.getId(), updatesUser);
        return "redirect:/admin/";
    }


}
