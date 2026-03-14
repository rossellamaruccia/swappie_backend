package com.example.swappie_be.Controllers;

import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.UnauthorizedException;
import com.example.swappie_be.Services.AuthService;
import com.example.swappie_be.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountController {
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AccountController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new UnauthorizedException("Log in again");
        } else {
            return this.userService.findById(user.getId());
        }
    }
}
