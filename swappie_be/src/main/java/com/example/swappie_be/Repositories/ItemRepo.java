package com.example.swappie_be.Repositories;

import com.example.swappie_be.Entities.Category;
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

//    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.user WHERE user.id = :id")
//    Optional<Item> findItemsPerUser(@Param("id") UUID id);

    Optional<ArrayList<Item>> findAllByUserId(UUID user_id);

    @Query("SELECT i FROM Item i WHERE i.user.id <> :userId AND i.category = :category")
    List<Item> findAvailableItemsByCategory(@Param("userId") UUID userId, @Param("category") Category category);

    @Query(value = """
            SELECT *, 
                   ST_Distance(location, :userLocation) as distance_meters
            FROM items
            WHERE ST_DWithin(location, :userLocation, :radiusInMeters)
            ORDER BY distance_meters ASC
            """, nativeQuery = true)
    List<Item> findItemsNear(
            @Param("userLocation") Point userLocation,
            @Param("radiusInMeters") double radiusInMeters
    );

}
