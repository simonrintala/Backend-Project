package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.AcceptState;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.BookingStateProcessor;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.IStateHandler;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.RejectState;

import java.time.LocalDateTime;

/************************
 * AcceptRejectBookingMethod
 * ---
 * This class contains methods that override abstract methods in the parent class.
 * These methods dictate parts of the process that are different between changing booking status and
 * deleting a booking.
 * **********************/

public class AcceptRejectBookingMethod extends AcceptRejectDeleteBookingTemplate {

    private final BookingDTOConversionService bookingDTOConversionService;
    private IStateHandler bookingStatus;

    public AcceptRejectBookingMethod(UserAuthRepository userAuthRepository, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository, BookingStateProcessor bookingStateProcessor, BookingDTOConversionService bookingDTOConversionService) {
        super(userAuthRepository, idValidationService, listingRepository, bookingRepository, bookingStateProcessor);
        this.bookingDTOConversionService = bookingDTOConversionService;

    }

    @Override
    void setVariables(String id, IStateHandler bookingStatus) {
        super.setVariables(id, bookingStatus);
        this.bookingStatus = bookingStatus;
    }
    @Override
    void validateOperation() {
        //check that booking status is pending
        if (!booking.getBookingStatus().equals("PENDING")) {
            throw new UnsupportedOperationException("Booking has already been accepted or rejected");
        }

        //check that current user is the host of the listing the booking refers to, otherwise cast error
        if (!userAuthRepository.isSameAsCurrentUser(listing.getHost())) {
            throw new UnauthorizedException("only the listing host can accept/reject a booking");
        }
    }


    @Override
    boolean modifyListingDatesCheck() {
        return (bookingStatus instanceof RejectState);
    }

    @Override
    BookingResponse updateBooking(String bookingId) {
            if (bookingStatus instanceof AcceptState) {
                bookingStateProcessor.accept(booking, listing, listingRepository);
            } else if (bookingStatus instanceof RejectState) {
                bookingStateProcessor.reject(booking, listing, listingRepository);
            } else {
                throw new IllegalArgumentException("Cannot set status of pending booking to other then accept/reject");
            }
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);
            return bookingDTOConversionService.convertToDTOResponse(booking);
    }

}
