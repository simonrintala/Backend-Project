package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import org.springframework.stereotype.Service;

@Service
public class CreateBookingMethod extends CreateUpdateBookingTemplate {

    public CreateBookingMethod(IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService) {
        super(idValidationService, userAuthRepository, bookingDTOConversionService, bookingRepository, dateAvailabilityService);
    }

    @Override
    void updateListingDatesAndSetPrice() {
        dateAvailabilityService.validateBookingDatesAndUpdateListing(booking, listing);
        calculateAndSetPrice(booking, listing);
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
