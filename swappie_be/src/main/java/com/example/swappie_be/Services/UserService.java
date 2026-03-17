package com.example.swappie_be.Services;

import com.cloudinary.utils.ObjectUtils;
import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Exceptions.NotFoundException;
import com.example.swappie_be.Payloads.UserDTO;
import com.example.swappie_be.Repositories.UserRepo;
import com.example.swappie_be.config.CloudinaryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryConfig cloudinaryConfig;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, CloudinaryConfig config) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryConfig = config;
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

    public User findByIdAndUpdate(UUID id, UserDTO payload, MultipartFile profilePic) {
        Optional<User> op = this.userRepo.findById(id);
        try {
            Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(profilePic.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            String profilePicUrl = uploadResult.get("secure_url").toString();
            if (op.isPresent()) {
                User user = op.get();
                user.setName(payload.name());
                user.setSurname(payload.surname());
                user.setEmail(payload.email());
                user.setCity(payload.city());
                user.setProfilePic(profilePicUrl);
                this.userRepo.save(user);
                return user;
            } else throw new NotFoundException(id);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    public User findByIdAndSetLocation(User user) {
        Optional<User> op = this.userRepo.findById(user.getId());
        try {
            if (op.isPresent()) {
                User found = op.get();
                found.setLocation(user.getLocation());
                return this.userRepo.save(found);
            } else throw new NotFoundException(user.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update your location", e);
        }
    }
}
