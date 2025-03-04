package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    // Find all bookings for a specific user
    List<Booking> findByUser(User user);
    // Find all bookings for a specific listing
    List<Booking> findByListing(Listing listing);
    // Find a booking by user and listing
    Optional<Booking> findByUserAndListing(User user, Listing listing);
    // Find all bookings for a specific user that have ended (end date is in the past)
    List<Booking> findByUserAndBookingDates_EndDateBefore(User user, LocalDateTime endDate);
    // Find all bookings for a specific listing that have ended (end date is in the past)
    List<Booking> findByListingAndBookingDates_EndDateBefore(Listing listing, LocalDateTime endDate);
}
