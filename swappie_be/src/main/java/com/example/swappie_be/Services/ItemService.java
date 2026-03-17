package com.example.swappie_be.Services;

import com.cloudinary.utils.ObjectUtils;
import com.example.swappie_be.Entities.Item;
import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.NotFoundException;
import com.example.swappie_be.Payloads.ItemDTO;
import com.example.swappie_be.Repositories.ItemRepo;
import com.example.swappie_be.config.CloudinaryConfig;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ItemService {
    private ItemRepo itemRepo;
    private UserService userService;
    private CloudinaryConfig cloudinaryConfig;

    @Autowired
    public ItemService(ItemRepo itemRepo, UserService userService, CloudinaryConfig cloudinaryConfig) {
        this.itemRepo = itemRepo;
        this.userService = userService;
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public Item save(ItemDTO payload, User user, MultipartFile[] files, Point itemPoint) {
        List<String> imageUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                    imageUrls.add((String) uploadResult.get("secure_url"));
                }
            }
            Item item = new Item();
            item.setTitle(payload.title());
            item.setDescription(payload.description());
            item.setType(payload.itemType());
            item.setPics(imageUrls);
            item.setUser(user);
            item.setLocation(itemPoint);

            return itemRepo.save(item);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    public ArrayList<Item> findItemsPerUserId(UUID user_id) {
        Optional<ArrayList<Item>> optional = this.itemRepo.findAllByUserId(user_id);
        if (optional.isPresent()) return optional.get();
        else throw new NotFoundException(user_id);
    }
}
