package com.Java24GroupProject.AirBnBPlatform.services.BookingMethodTemplates;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import com.Java24GroupProject.AirBnBPlatform.services.PriceCalculationService;

public class BookingCreateMethod extends BookingCreateUpdateTemplate {

    public BookingCreateMethod(IdValidationService idValidationService, AuthenticationService authenticationService, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService, PriceCalculationService priceCalculationService) {
        super(idValidationService, authenticationService, bookingDTOConversionService, bookingRepository, dateAvailabilityService, priceCalculationService);
    }

    @Override
    void updateListingDatesAndSetPrice() {
        dateAvailabilityService.validateBookingDatesAndUpdateListing(booking, listing);
    }

    @Override
    void convertRequestDTOtoBooking(BookingRequest updatedBookingRequest) {
        booking = bookingDTOConversionService.convertRequestToBooking(updatedBookingRequest);
    }

    @Override
    void setRemainingFields() {
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setUpdatedAt(null);
    }

}
