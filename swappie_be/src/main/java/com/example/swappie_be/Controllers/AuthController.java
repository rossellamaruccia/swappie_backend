package com.example.swappie_be.Controllers;

import com.example.swappie_be.Exceptions.BadRequestException;
import com.example.swappie_be.Exceptions.ValidationException;
import com.example.swappie_be.Payloads.LoginDTO;
import com.example.swappie_be.Payloads.LoginResponseDTO;
import com.example.swappie_be.Payloads.UserDTO;
import com.example.swappie_be.Services.AuthService;
import com.example.swappie_be.Services.UserService;
import com.example.swappie_be.config.Geometry;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final Geometry geometry;

    @Autowired
    public AuthController(AuthService authService, UserService userService, Geometry geometry) {
        this.authService = authService;
        this.userService = userService;
        this.geometry = geometry;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        String accessToken = this.authService.checkCredentialAndGenerateToken(body);
        UUID id = this.userService.returnID(body.email());
        return new LoginResponseDTO(accessToken, id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Validated UserDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorList);
        }
        if (payload.location().lng() == null || payload.location().lat() == null) {
            throw new BadRequestException("Error: Location is required to register.");
        } else {
            Point userPoint = this.geometry.geometryFactory().createPoint(new Coordinate(payload.location().lng(), payload.location().lat()));
            this.userService.save(payload, userPoint);
        }
    }
}
