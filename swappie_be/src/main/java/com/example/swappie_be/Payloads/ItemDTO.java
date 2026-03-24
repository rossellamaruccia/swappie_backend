package com.example.swappie_be.Payloads;

import com.example.swappie_be.Entities.Category;
import com.example.swappie_be.Entities.ItemType;

public record ItemDTO(
        String title,
        String description,
        ItemType itemType,
        Category category,
        LocationDTO location
) {
}
