package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    // Find all reviews for a specific listing
    List<Review> findByListing_Id(String listingId);
    // Find all reviews for a specific user
    List<Review> findByUser_Id(String userId);
    // Find all reviews for a specific host (via the listing's host)
    List<Review> findByListing_Host_Id(String hostId);
}
