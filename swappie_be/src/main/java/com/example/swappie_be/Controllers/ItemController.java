package com.example.swappie_be.Controllers;

import com.example.swappie_be.Entities.Item;
import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.UnauthorizedException;
import com.example.swappie_be.Exceptions.ValidationException;
import com.example.swappie_be.Payloads.ItemDTO;
import com.example.swappie_be.Payloads.LocationDTO;
import com.example.swappie_be.Services.ItemService;
import com.example.swappie_be.config.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final Geometry geometry;

    public ItemController(ItemService itemService, Geometry geometry) {
        this.itemService = itemService;
        this.geometry = geometry;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Item createNewItem(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Validated ItemDTO payload,
            @RequestBody LocationDTO location,
            @RequestParam("files") MultipartFile[] files,
            BindingResult validationResult
    ) {
        if (validationResult.hasErrors()) {
            List<String> errorList = validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorList);
        }

        if (user == null) {
            throw new UnauthorizedException("Log in again");
        }

        Point itemPoint = geometry.createPoint(location.lng(), location.lat());
        return this.itemService.save(payload, user, files, itemPoint);
    }

    @GetMapping("")
    public ArrayList<Item> getItemsPerUser(@AuthenticationPrincipal User user) {
        return this.itemService.findItemsPerUserId(user.getId());
    }

}
