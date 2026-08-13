package com.example.swappie_be.Services;

import com.example.swappie_be.Repositories.BorrowInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {
    private BorrowInfoRepo borrowInfoRepo;

    @Autowired
    public BorrowService(BorrowInfoRepo borrowInfoRepo) {
        this.borrowInfoRepo = borrowInfoRepo;
    }

    public void initiateBorrow() {
    }

    public void terminateBorrow() {
    }
}
