package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;

    public AdminController(UserService userService, RoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String userlistView(Model model, Principal principal) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("user", userRepository.findByUsername(principal.getName()));
        model.addAttribute("roles", roleService.getAll());
        return "admin/userlist";
    }


@GetMapping("/addUser")
public String createForm(@ModelAttribute("user") User user, Model model, Principal principal) {
    model.addAttribute("roles", roleService.getAll());
    model.addAttribute("userc", userRepository.findByUsername(principal.getName()));
    return "admin/addUserField";
}

    @PostMapping("/addOrUpdate")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/addUserField";
        user.setPassword(user.getPassword());
        userService.create(user);
        return "redirect:/admin";
    }

    @PostMapping("/removeUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping ("/{id}/addOrUpdate")
    public String edit(@ModelAttribute("user") @Valid User user,@PathVariable("id") long id,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/addUserField";
        }
        user.setPassword(user.getPassword());
        userService.update(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/addOrUpdate")
    public String userEditForm(@PathVariable("id")Long id,Model model){
        model.addAttribute("user",userService.get(id));
        model.addAttribute("roles",roleService.getAll());
        return "admin/userlist";
    }

}
