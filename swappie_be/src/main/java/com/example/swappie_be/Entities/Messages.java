package com.example.swappie_be.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@Getter
@Setter
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long message_id;
    @OneToOne
    private User sender;
    @OneToOne
    private User receiver;
    private String message;
    private Date timestamp;

    public Messages(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = new Date();
    }
}
