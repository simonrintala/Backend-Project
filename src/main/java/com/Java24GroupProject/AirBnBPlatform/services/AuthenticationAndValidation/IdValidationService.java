package com.Java24GroupProject.AirBnBPlatform.services.IdValidation;

import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;

public class IdValidation implements IIdValidationService{
   private final BookingRepository bookingRepository;
   private final UserRepository userRepository;
   private final ListingRepository listingRepository;


    public IdValidation(BookingRepository bookingRepository, UserRepository userRepository, ListingRepository listingRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    Listing validateListingIdAndGetListing(String id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No listing with id '" + id + "' in database"));

    }
}
