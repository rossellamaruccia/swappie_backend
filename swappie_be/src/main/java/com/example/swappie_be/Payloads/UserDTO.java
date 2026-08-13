package com.example.swappie_be.Payloads;

public record UserDTO(
        String name,
        String surname,
        String username,
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,
        String password,
        String city,
        LocationDTO location) {
}
