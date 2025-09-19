package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;

/************************
 * CreateBookingMethod
 * ---
 * This class contains methods that override abstract methods in the parent class.
 * These methods dictate parts of the process that are different between creating a new booking
 * and updating an existing booking.
 * **********************/

public class CreateBookingMethod extends CreateUpdateBookingTemplate {

    public CreateBookingMethod(IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService) {
        super(idValidationService, userAuthRepository, bookingDTOConversionService, bookingRepository, dateAvailabilityService);
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
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setUpdatedAt(null);
    }

}
