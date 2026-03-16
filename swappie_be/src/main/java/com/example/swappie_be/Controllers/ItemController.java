package com.example.swappie_be.Controllers;

import com.example.swappie_be.Entities.Item;
import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.UnauthorizedException;
import com.example.swappie_be.Exceptions.ValidationException;
import com.example.swappie_be.Payloads.ItemDTO;
import com.example.swappie_be.Services.ItemService;
import com.example.swappie_be.security.JWTTools;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final JWTTools jwtTools;

    public ItemController(ItemService itemService, JWTTools jwtTools) {
        this.itemService = itemService;
        this.jwtTools = jwtTools;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Item createNewItem(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Validated ItemDTO payload,
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

        return this.itemService.save(payload, user, files);
    }

    @GetMapping("")
    public ArrayList<Item> getItemsPerUser(@RequestParam(name = "user") String user_id) {
        return this.itemService.findItemsPerUserId(UUID.fromString(user_id));
    }

}
