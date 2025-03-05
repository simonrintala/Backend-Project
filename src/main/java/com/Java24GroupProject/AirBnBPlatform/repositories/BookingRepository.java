package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUser(User user);
    List<Booking> findByListing(Listing listing);
    Long deleteByListing(Listing listing);
    List<Booking> deleteByUser(User user);
    // Find a booking by user and listing
    Optional<Booking> findByUserAndListing(User user, Listing listing);
    // Find all bookings for a specific user that have ended (end date is in the past)
}
