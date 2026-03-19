package com.example.swappie_be.Payloads;

import com.example.swappie_be.Entities.ItemType;

import java.util.List;
import java.util.UUID;

public record ItemGetResponseDTO(
        long id,
        String title,
        String description,
        ItemType type,
        UUID user_id,
        List<String> pics_urls,
        double lng,
        double lat
) {
}
