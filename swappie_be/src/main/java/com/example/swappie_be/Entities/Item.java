package com.example.swappie_be.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@NoArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    @ElementCollection
    private List<String> pics = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Item(String title, String description, List<String> pics, User user) {
        this.title = title;
        this.description = description;
        this.pics = pics;
        this.user = user;
    }
}
