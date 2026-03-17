package com.example.swappie_be.Repositories;

import com.example.swappie_be.Entities.Item;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.user WHERE user.id = :id")
    Optional<Item> findItemsPerUser(@Param("id") UUID id);

    Optional<ArrayList<Item>> findAllByUserId(UUID user_id);

    @Query(value = "SELECT * FROM items i WHERE ST_DWithin(i.location, :userLocation, :radiusInMeters)",
            nativeQuery = true)
    List<Item> findItemsNear(@Param("userLocation") Point userLocation,
                             @Param("radiusInMeters") double radiusInMeters);
}
