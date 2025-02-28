package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByBooking_Listing_Id(String listingId);
    List<Review> findByBooking_User_Id(String userId);
    List<Review> findByBooking_Listing_Host_Id(String hostId);
}
