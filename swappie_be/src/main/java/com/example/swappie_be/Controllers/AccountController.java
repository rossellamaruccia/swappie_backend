package com.example.swappie_be.Controllers;

import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.UnauthorizedException;
import com.example.swappie_be.Exceptions.ValidationException;
import com.example.swappie_be.Payloads.LocationDTO;
import com.example.swappie_be.Payloads.UserDTO;
import com.example.swappie_be.Services.UserService;
import com.example.swappie_be.config.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AccountController {
    private final UserService userService;
    private final Geometry geometry;

    @Autowired
    public AccountController(UserService userService, Geometry geometry) {
        this.geometry = geometry;
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

    @PutMapping(value = "/me/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User getUserByIdAndUpdate(@AuthenticationPrincipal User user, @ModelAttribute @Validated UserDTO payload, MultipartFile profilePic, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorList = validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorList);
        }
        if (user == null) {
            throw new UnauthorizedException("Log in again");
        } else return this.userService.findByIdAndUpdate(user.getId(), payload, profilePic);
    }

    @PostMapping("/locate")
    public User receiveLocation(@AuthenticationPrincipal User user, @RequestBody LocationDTO location) {
        Point userPoint = geometry.createPoint(location.lng(), location.lat());
        user.setLocation(userPoint);
        return this.userService.findByIdAndSetLocation(user);
    }
}
