package com.example.swappie_be.Payloads;

import java.util.UUID;

public record LoginResponseDTO(String accessToken, UUID id) {
}
