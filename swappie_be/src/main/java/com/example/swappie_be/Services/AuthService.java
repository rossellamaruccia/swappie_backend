package com.example.swappie_be.Services;

import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.UnauthorizedException;
import com.example.swappie_be.Payloads.LoginDTO;
import com.example.swappie_be.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(UserService userService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.userService = userService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body) {
        User found = this.userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("Wrong password");
        }
    }
}
