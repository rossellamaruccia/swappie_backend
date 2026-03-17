package com.example.swappie_be.Repositories;

import com.example.swappie_be.Entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE ST_DWithin(u.location, :currentLocation, :distance)",
            nativeQuery = true)
    List<User> findNearbyUsers(@Param("currentLocation") Point currentLocation,
                               @Param("distance") double distance);
}
