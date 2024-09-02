package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.index());
        return "index";
    }

    @GetMapping("/new")
    public String drawNewPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute(
                "roles",
                roleService.getAllRoles().stream()
                        .map(Role::getRole)
                        .collect(Collectors.toList())
        );
        return "new";
    }

    @PostMapping("/create")
    public String createNewUser(@ModelAttribute User user, @RequestParam("roles") List<String> roles) {
        userService.save(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam(value = "id", required = false) Integer id) {
        model.addAttribute("user", userService.show(id));
        return "edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}