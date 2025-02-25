package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ListingRepository extends MongoRepository<Listing, String> {
    //find based on location (city)
    List<Listing> findByLocation(String location);
    
    //find based on price interval
    List<Listing> findByPricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
}