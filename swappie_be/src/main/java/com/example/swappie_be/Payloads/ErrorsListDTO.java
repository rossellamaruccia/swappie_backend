package com.example.swappie_be.Payloads;

import java.util.List;

public record ErrorsListDTO(String message, List<String> errors) {
}
