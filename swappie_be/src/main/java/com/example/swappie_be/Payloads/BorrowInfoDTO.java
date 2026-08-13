package com.example.swappie_be.Payloads;

import com.example.swappie_be.Entities.Item;
import com.example.swappie_be.Entities.User;

import java.util.Date;

public record BorrowInfoDTO(Item item, User user, Date date) {
}