package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ListingRepository extends MongoRepository<Listing, String> {
    // Find all listings for a specific host
    List<Listing> findByHost(User host);
    // Find all listings in a specific location
    List<Listing> findByLocation(String location);
    // Find a listing by ID
    Optional<Listing> findById(String id);
    // Find listings with a minimum average rating
    List<Listing> findByAverageRatingGreaterThanEqual(double minRating);
}
