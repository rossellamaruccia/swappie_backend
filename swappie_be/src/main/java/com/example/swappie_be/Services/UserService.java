package com.example.swappie_be.Services;

import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.NotFoundException;
import com.example.swappie_be.Payloads.UserDTO;
import com.example.swappie_be.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(UserDTO payload) {
        User newUser = new User(payload.name(), payload.surname(), payload.email(), passwordEncoder.encode(payload.password()), payload.city());
        return this.userRepo.save(newUser);
    }

    public User findById(UUID id) {
        Optional<User> op = this.userRepo.findById(id);
        if (op.isPresent()) return op.get();
        else throw new NotFoundException(id);
    }

    public User findByEmail(String email) {
        Optional<User> op = this.userRepo.findByEmail(email);
        if (op.isPresent()) return op.get();
        else throw new NotFoundException("Email non registrata.");
    }
}
