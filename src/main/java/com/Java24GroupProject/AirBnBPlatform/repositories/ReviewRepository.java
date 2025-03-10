package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    // Find all reviews for a specific listing
    List<Review> findByListing_Id(String listingId);
    // Find all reviews for a specific user
    List<Review> findByUser_Id(String userId);
    List<Review> findByUser(User user);
    void deleteByListing(Listing listing);


/*  Not used at the moment
    // Find all reviews for a specific host (via the listing's host)
    @Query("{ 'listing.host.$id.$oid': ?0 }")
    List<Review> findByListing_Host_Id(String hostId); */

}
