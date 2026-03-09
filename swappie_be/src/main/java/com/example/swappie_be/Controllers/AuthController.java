package com.example.swappie_be.Controllers;

import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.ValidationException;
import com.example.swappie_be.Payloads.LoginDTO;
import com.example.swappie_be.Payloads.LoginResponseDTO;
import com.example.swappie_be.Payloads.UserDTO;
import com.example.swappie_be.Services.AuthService;
import com.example.swappie_be.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkCredentialAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated UserDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorList);
        } else {
            return this.userService.save(payload);
        }
    }
}
