package com.example.swappie_be.Repositories;

import com.example.swappie_be.Entities.BorrowInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowInfoRepo extends JpaRepository<BorrowInfo, Long> {
}
