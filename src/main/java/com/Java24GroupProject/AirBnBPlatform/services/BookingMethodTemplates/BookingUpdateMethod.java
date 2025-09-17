package com.Java24GroupProject.AirBnBPlatform.services.BookingMethodTemplates;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingUpdateMethod extends BookingCreateUpdateTemplate {
    private Booking updatedBooking;

    public BookingUpdateMethod(IdValidationService idValidationService, AuthenticationService authenticationService, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService) {
        super(idValidationService, authenticationService, bookingDTOConversionService, bookingRepository, dateAvailabilityService);
    }

    @Override
    void setModels(BookingRequest bookingRequest, String bookingId) {
        super.setModels(bookingRequest, bookingId);
        booking = idValidationService.validateBookingIdAndReturnBooking(bookingId);
    }

    @Override
    void validateBookingRequest(BookingRequest updatedBookingRequest) {
        super.validateBookingRequest(updatedBookingRequest);

        if (!authenticationService.isSameAsCurrentUser(booking.getUser())) {
            throw new UnauthorizedException("Only the owner of the booking can update the booking");
        }

        //check if status is pending, otherwise cannot be changed
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Accepted or rejected bookings cannot be updated");
        }

        //listing of booking cannot be changed
        if (!booking.getListing().getId().equals(updatedBookingRequest.getListingId())) {
            throw new IllegalArgumentException("Listing cannot be changed");
        }
    }

    @Override
    void convertRequestDTOtoBooking(BookingRequest updatedBookingRequest) {
        updatedBooking = bookingDTOConversionService.convertRequestToBooking(updatedBookingRequest);
    }

    @Override
    void updateListingDatesAndSetPrice() {
        if (!booking.getBookingDates().getStartDate().equals(updatedBooking.getBookingDates().getStartDate()) ||
                !booking.getBookingDates().getEndDate().equals(updatedBooking.getBookingDates().getEndDate())) {

            dateAvailabilityService.changeBookingDatesForListing(listing, booking, updatedBooking);
            calculateAndSetPrice(booking, listing);
        }
    }

    @Override
    void setRemainingFields() {
        booking.setNumberOfGuests(booking.getNumberOfGuests());
        booking.setUpdatedAt(LocalDateTime.now());
    }
}
