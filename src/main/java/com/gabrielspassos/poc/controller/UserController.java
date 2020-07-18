package com.gabrielspassos.poc.controller;

import com.gabrielspassos.poc.controller.request.UserRequest;
import com.gabrielspassos.poc.dto.UserDTO;
import com.gabrielspassos.poc.exception.BusinessException;
import com.gabrielspassos.poc.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public String showIndexForm() {
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(UserRequest userRequest) {
        return "add-user";
    }

    @PostMapping("/users")
    public String addUser(UserRequest userRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        try {
            userService.createUser(userRequest);
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("users", userService.getActiveUsers());
        return "index";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        UserDTO user = userService.getUser(id);

        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/users/{id}")
    public String updateUser(@PathVariable("id") Long id, UserRequest userRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-user";
        }

        try {
            userService.updateUser(id, userRequest);
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("users", userService.getActiveUsers());
        return "index";
    }

    @GetMapping("/delete/users/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        userService.deleteUser(id);

        model.addAttribute("users", userService.getActiveUsers());
        return "index";
    }
}
