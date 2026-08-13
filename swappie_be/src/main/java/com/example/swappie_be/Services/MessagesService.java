package com.example.swappie_be.Services;

import com.example.swappie_be.Repositories.MessagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {
    public MessagesRepo messagesRepo;

    @Autowired
    public MessagesService(MessagesRepo messagesRepo) {
        this.messagesRepo = messagesRepo;
    }
}
