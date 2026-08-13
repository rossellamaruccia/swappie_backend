package com.example.swappie_be.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.*;

@Entity
@Table(name = "items")
@NoArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long item_id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private ItemType type;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;
    @Transient
    private Double distanceMeters;
    private List<String> pics = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User user;


    @ManyToMany(mappedBy = "favouriteItems")
    private Set<User> usersFav = new HashSet<>();

    public Item(String title, String description, List<String> pics, ItemType type, Category category, User user) {
        this.title = title;
        this.description = description;
        this.pics = pics;
        this.type = type;
        this.category = category;
        this.user = user;
    }

    public UUID getUserId() {
        return user.getUser_id();
    }

    public int getUsersFavNumber() {
        return usersFav.size();
    }
}
