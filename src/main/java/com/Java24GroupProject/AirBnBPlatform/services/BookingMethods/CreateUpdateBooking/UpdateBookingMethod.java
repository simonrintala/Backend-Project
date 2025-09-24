package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;

import java.time.LocalDateTime;

/************************
 * UpdateBookingMethod
 * ---
 * This class contains methods that override abstract methods in the parent class.
 * These methods dictate parts of the process that are different between creating a new booking
 * and updating an existing booking.
 * **********************/

public class UpdateBookingMethod extends CreateUpdateBookingTemplate {
    private Booking updatedBooking;

    public UpdateBookingMethod(IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService) {
        super(idValidationService, userAuthRepository, bookingDTOConversionService, bookingRepository, dateAvailabilityService);
    }

    //includes the super-method but also saves the current booking (which is to be updated) as a class variable
    @Override
    void setVariables(BookingRequest bookingRequest, String bookingId) {
        super.setVariables(bookingRequest, bookingId);
        booking = idValidationService.validateBookingIdAndReturnBooking(bookingId);
    }

    //includes the super-method and adds checks regarding the owner and status of the booking that is to be updated
    @Override
    void validateBookingRequest(BookingRequest updatedBookingRequest) {
        super.validateBookingRequest(updatedBookingRequest);

        if (!userAuthRepository.isSameAsCurrentUser(booking.getUser())) {
            throw new UnauthorizedException("Only the owner of the booking can update the booking");
        }

        //check if status is pending, otherwise cannot be changed
        if (!booking.getBookingStatus().equals("PENDING")) {
            throw new UnsupportedOperationException("Accepted or rejected bookings cannot be updated");
        }

        //listing of booking cannot be changed
        if (!booking.getListing().getId().equals(updatedBookingRequest.getListingId())) {
            throw new IllegalArgumentException("Listing cannot be changed");
        }
    }

    //converts the bookingRequest to a booking in order to perform the updateListingDatesAndSetPrice method
    @Override
    void convertRequestDTOtoBooking(BookingRequest updatedBookingRequest) {
        updatedBooking = bookingDTOConversionService.convertRequestToBooking(updatedBookingRequest);
    }

    //updates the booking dates IF they differ between the updatedBooking and the current booking
    @Override
    void updateListingDatesAndSetPrice() {
        if (!booking.getBookingDates().getStartDate().equals(updatedBooking.getBookingDates().getStartDate()) ||
                !booking.getBookingDates().getEndDate().equals(updatedBooking.getBookingDates().getEndDate())) {

            dateAvailabilityService.changeBookingDatesForListing(listing, booking, updatedBooking);
            priceContext.runCalculation(booking, listing);
        }
    }

    //sets number of guests according to new request and sets UpdatedAt
    @Override
    void setRemainingFields() {
        booking.setNumberOfGuests(booking.getNumberOfGuests());
        booking.setUpdatedAt(LocalDateTime.now());
    }
}
