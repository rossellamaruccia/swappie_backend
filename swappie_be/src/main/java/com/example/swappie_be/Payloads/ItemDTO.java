package com.example.swappie_be.Payloads;

import java.util.List;
import java.util.UUID;

public record ItemDTO(String title, String description, List<String> pics, UUID user_id) {
}
