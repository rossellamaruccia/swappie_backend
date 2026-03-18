package com.example.swappie_be.Payloads;

public record UserGetResponseDTO(
        String name,
        String surname,
        String email,
        String city,
        String profilePic,
        LocationDTO location) {
}
