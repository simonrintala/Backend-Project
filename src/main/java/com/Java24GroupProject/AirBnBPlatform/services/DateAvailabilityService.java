package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

import java.time.LocalDateTime;

/**
 * Class for validating date availability checks and syncing when making/updating a booking.
 * under construction.
  */


public interface DateAvailabilityService {
    //validate that booking dates are available and update listing dates
    default void validateBookingDatesAndUpdateListing(Booking booking, Listing listing, ListingRepository listingRepository) {

        //save booking dates in variable for ease of use
        DateRange bookingDates = booking.getBookingDates();

        //check that booking dates fall within available dates of listing.
        boolean areBookingDatesAvailable = false;
        for (DateRange availibleDateRange : listing.getAvailableDates()) {
            if (bookingDates.isWithinAnotherDateRange(availibleDateRange)) {
                areBookingDatesAvailable = true;

                //update listing dates
                if (bookingDates.isIdenticalToAnotherDateRange(availibleDateRange)) {
                    listing.getAvailableDates().remove(availibleDateRange);
                } else if (bookingDates.getStartDate().isEqual(availibleDateRange.getStartDate())) {
                    availibleDateRange.setStartDate(bookingDates.getEndDate());
                } else if (bookingDates.getEndDate().isEqual(availibleDateRange.getEndDate())) {
                    availibleDateRange.setEndDate(bookingDates.getStartDate());
                } else {
                    DateRange newDateRange = new DateRange(bookingDates.getEndDate(), availibleDateRange.getEndDate());
                    listing.getAvailableDates().add(newDateRange);
                    availibleDateRange.setEndDate(bookingDates.getStartDate());
                }

                //save updated listing
                listing.setUpdatedAt(LocalDateTime.now());
                listingRepository.save(listing);
                break;
            }
        }

        //error if bookingDates are not available in listing
        if (!areBookingDatesAvailable) {
            throw new IllegalArgumentException("booking dates not available on listing");
        }
    }
}
