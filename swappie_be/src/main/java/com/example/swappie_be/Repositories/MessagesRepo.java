package com.example.swappie_be.Repositories;

import com.example.swappie_be.Entities.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepo extends JpaRepository<Messages, Long> {
}
