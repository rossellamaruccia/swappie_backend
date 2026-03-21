package com.example.swappie_be.Services;

import com.cloudinary.utils.ObjectUtils;
import com.example.swappie_be.Entities.Category;
import com.example.swappie_be.Entities.Item;
import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.NotFoundException;
import com.example.swappie_be.Payloads.ItemDTO;
import com.example.swappie_be.Payloads.ItemGetResponseDTO;
import com.example.swappie_be.Repositories.ItemRepo;
import com.example.swappie_be.config.CloudinaryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private ItemRepo itemRepo;
    private CloudinaryConfig cloudinaryConfig;

    @Autowired
    public ItemService(ItemRepo itemRepo, CloudinaryConfig cloudinaryConfig) {
        this.itemRepo = itemRepo;
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public void save(ItemDTO payload, User user, MultipartFile[] files) {
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
            item.setCategory(payload.category());
            item.setPics(imageUrls);
            item.setUser(user);
            item.setLocation(user.getLocation());
            this.itemRepo.save(item);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    public ArrayList<ItemGetResponseDTO> findItemsPerUserId(UUID user_id) {
        Optional<ArrayList<Item>> optional = this.itemRepo.findAllByUserId(user_id);
        if (optional.isPresent()) {
            ArrayList<Item> array = optional.get();
            return array.stream()
                    .map(item -> new ItemGetResponseDTO(
                            item.getId(),
                            item.getTitle(),
                            item.getDescription(),
                            item.getType(),
                            item.getCategory(),
                            item.getUser().getId(),
                            item.getPics(),
                            item.getLocation().getX(),
                            item.getLocation().getY()
                    ))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else throw new NotFoundException(user_id);
    }

    public List<ItemGetResponseDTO> findAllItems(User user) {
        List<Item> allItemsList = this.itemRepo.findAll();
        List<ItemGetResponseDTO> allGetResponseItemsList = allItemsList.stream()
                .map(item -> new ItemGetResponseDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getType(),
                        item.getCategory(),
                        item.getUser().getId(),
                        item.getPics(),
                        item.getLocation().getX(),
                        item.getLocation().getY()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
        allGetResponseItemsList.removeIf((item -> user.getId().equals(item.user_id())));
        return allGetResponseItemsList;// questa funzione deve ritornare tutti gli item tranne quelli dell'user che fa la richiesta
    }

    public List<ItemGetResponseDTO> findAllByCategory(User user, Category category) {
        return this.itemRepo.findAvailableItemsByCategory(user.getId(), category).stream()
                .map(item -> new ItemGetResponseDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getType(),
                        item.getCategory(),
                        item.getUser().getId(),
                        item.getPics(),
                        item.getLocation().getX(),
                        item.getLocation().getY()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
