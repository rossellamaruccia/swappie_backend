package com.example.swappie_be.Payloads;

import com.example.swappie_be.Entities.ItemType;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ItemDTO(
        String title,
        String description,
        ItemType itemType,
        @Size(max = 5, message = "You cannot upload more than 5 images")
        List<MultipartFile> files,
        LocationDTO location
) {
}
