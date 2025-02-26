package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByListing_Id(String listingId);
    List<Review> findByUser_Id(String userId);
}
