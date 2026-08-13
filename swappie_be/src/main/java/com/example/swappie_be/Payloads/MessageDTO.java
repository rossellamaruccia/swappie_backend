package com.example.swappie_be.Payloads;

import com.example.swappie_be.Entities.User;

public record MessageDTO(User sender, User receiver, String message) {
}
