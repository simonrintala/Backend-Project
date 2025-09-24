package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.BookingStateProcessor;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.PendingState;

/************************
 * CreateBookingMethod
 * ---
 * This class contains methods that override abstract methods in the parent class.
 * These methods dictate parts of the process that are different between creating a new booking
 * and updating an existing booking.
 * **********************/

public class CreateBookingMethod extends CreateUpdateBookingTemplate {
    private final BookingStateProcessor bookingStateProcessor;
    private final ListingRepository listingRepository;

    public CreateBookingMethod(IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService, BookingStateProcessor bookingStateProcessor, ListingRepository listingRepository) {
        super(idValidationService, userAuthRepository, bookingDTOConversionService, bookingRepository, dateAvailabilityService);
        this.bookingStateProcessor = bookingStateProcessor;
        this.listingRepository = listingRepository;
    }

    @Override
    void updateListingDatesAndSetPrice() {
        dateAvailabilityService.validateBookingDatesAndUpdateListing(booking, listing);
        priceContext.runCalculation(booking, listing);
    }

    //makes a booking from the bookingRequest and saves it as a class variable
    @Override
    void convertRequestDTOtoBooking(BookingRequest updatedBookingRequest) {
        booking = bookingDTOConversionService.convertRequestToBooking(updatedBookingRequest);
    }

    //sets status as pending for a new booking
    @Override
    void setRemainingFields() {
        bookingStateProcessor.applyState(new PendingState(), booking, listing, listingRepository);
        booking.setUpdatedAt(null);
    }

}
