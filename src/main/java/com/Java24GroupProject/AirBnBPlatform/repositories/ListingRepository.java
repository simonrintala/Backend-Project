package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface ListingRepository extends MongoRepository<Listing, String> {

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'averageRating': ?1 } }")
    void updateAverageRating(String ListingId, double averageRating);
}
