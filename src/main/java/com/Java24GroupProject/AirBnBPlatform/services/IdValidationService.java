package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

/************************
 * IdValidationService
 * ---
 * This class contain the methods for validating object IDs against the MongoDB database (via the different repositories).
 * These methods were previously static methods in the UserService, ListingService and BookingService classes, respectively,
 * and were separated into this class instead based on the Single Responsibility Principle and to decouple the mentioned service classes
 * **********************/

@Service
public class IdValidationService {
   private final BookingRepository bookingRepository;
   private final UserRepository userRepository;
   private final ListingRepository listingRepository;


    public IdValidationService(UserRepository userRepository, ListingRepository listingRepository, BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    public User validateUserIdAndReturnUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user with id '"+ id + "' in database"));
    }

    public Listing validateListingIdAndReturnListing(String id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No listing with id '" + id + "' in database"));
    }

    public Booking validateBookingIdAndReturnBooking(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No booking with id '" + id + "' in database"));
    }

}
