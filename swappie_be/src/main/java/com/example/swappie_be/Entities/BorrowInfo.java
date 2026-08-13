package com.example.swappie_be.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Borrow_info")
@NoArgsConstructor
@Getter
@Setter
public class BorrowInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long item_id;
    @OneToOne
    private Item item;
    @OneToOne
    private User user;
    private Date borrowDate;
    private Date returnDate;

    public BorrowInfo(Item item, User user, int dd, int mm, int yyyy) {
        this.item = item;
        this.user = user;
        this.borrowDate = new Date(yyyy, mm, dd);
        this.returnDate = new Date(yyyy, mm, dd + 7);
    }

    //TODO: cancel entry when the item is returned (service methods)
    //UI Item page and User page need to access this info, too. How?
}
