package com.example.swappie_be.Services;

import com.example.swappie_be.Entities.Item;
import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Payloads.ItemDTO;
import com.example.swappie_be.Repositories.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private ItemRepo itemRepo;
    private UserService userService;

    @Autowired
    public ItemService(ItemRepo itemRepo, UserService userService) {
        this.itemRepo = itemRepo;
        this.userService = userService;
    }

    public Item save(ItemDTO payload) {
        User user = userService.findById(payload.user_id());
        Item newItem = new Item(payload.title(), payload.description(), payload.pics(), user);
        return this.itemRepo.save(newItem);
    }
}
