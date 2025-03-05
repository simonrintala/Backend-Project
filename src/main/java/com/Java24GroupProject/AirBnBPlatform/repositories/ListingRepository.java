package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ListingRepository extends MongoRepository<Listing, String> {
    //find based on host
    List<Listing> findByHostId(String hostId);
    //find based on location (city)
    List<Listing> findByLocation(String location);
    //delete by user
    List<Listing> deleteByHost(User user);
    //find based on price interval
    // query to get listings that matches min/max price. https://stackoverflow.com/questions/32846996/mongodb-query-using-gte-and-lte-in-java
    @Query("{ 'pricePerNight': { $gte: ?0, $lte: ?1 } }")
    List<Listing> findByPricePerNightBetween(double minPrice, double maxPrice);
    @Query("{ 'capacity': { $gte:  ?0, $lte:  ?1 } }")
    List<Listing> findByCapacityBetween(double minCapacity, double maxCapacity);
    List<Listing> findByUtilities(String utility);
    // Find a listing by ID
    Optional<Listing> findById(String id);

}
