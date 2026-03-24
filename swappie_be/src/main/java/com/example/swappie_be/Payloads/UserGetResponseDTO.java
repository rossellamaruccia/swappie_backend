package com.example.swappie_be.Payloads;

import java.util.UUID;

public record UserGetResponseDTO(
        UUID id,
        String name,
        String surname,
        String email,
        String city,
        String profilePic,
        LocationDTO location) {
}
