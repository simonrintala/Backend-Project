package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;

import java.time.LocalDateTime;

public class AcceptRejectBooking extends AcceptRejectDeleteTemplate {

    private final BookingDTOConversionService bookingDTOConversionService;
    private BookingStatus bookingStatus;

    public AcceptRejectBooking(UserAuthRepository userAuthRepository, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository, BookingDTOConversionService bookingDTOConversionService) {
        super(userAuthRepository, idValidationService, listingRepository, bookingRepository);
        this.bookingDTOConversionService = bookingDTOConversionService;
    }

    @Override
    void setVariables(String id, BookingStatus bookingStatus) {
        super.setVariables(id, bookingStatus);
        this.bookingStatus = bookingStatus;
    }
    @Override
    void validateOperation() {
        //check that booking status is pending
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Booking has already been accepted or rejected");
        }

        //check that current user is the host of the listing the booking refers to, otherwise cast error
        if (!userAuthRepository.isSameAsCurrentUser(listing.getHost())) {
            throw new UnauthorizedException("only the listing host can accept/reject a booking");
        }
    }


    @Override
    boolean modifyListingDatesCheck() {
        return (bookingStatus == BookingStatus.REJECTED);
    }

    @Override
    BookingResponse updateBooking(String bookingId) {
            booking.setBookingStatus(bookingStatus);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);
            return bookingDTOConversionService.convertToDTOResponse(booking);
    }

}
