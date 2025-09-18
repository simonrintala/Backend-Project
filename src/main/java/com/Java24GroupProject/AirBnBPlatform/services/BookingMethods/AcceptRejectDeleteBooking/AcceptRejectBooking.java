package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;

import java.time.LocalDateTime;

public class AcceptRejectBooking extends AcceptRejectDeleteTemplate {

    private final BookingDTOConversionService bookingDTOConversionService;
    private boolean isAccepted;

    public AcceptRejectBooking(AuthenticationService authenticationService, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository, BookingDTOConversionService bookingDTOConversionService) {
        super(authenticationService, idValidationService, listingRepository, bookingRepository);
        this.bookingDTOConversionService = bookingDTOConversionService;
    }

    @Override
    void setVariables(String id, boolean isAccepted) {
        super.setVariables(id, isAccepted);
        this.isAccepted = isAccepted;
    }
    @Override
    void validateOperation() {
        //check that booking status is pending
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Booking has already been accepted or rejected");
        }

        //check that current user is the host of the listing the booking refers to, otherwise cast error
        if (!authenticationService.isSameAsCurrentUser(listing.getHost())) {
            throw new UnauthorizedException("only the listing host can accept/reject a booking");
        }
    }


    @Override
    void modifyListingDates() {
        //if booking does not have status denied, add back the booked dates to the listing
        if (booking.getBookingStatus() != BookingStatus.REJECTED) {
            listing.addAvailableDateRange(booking.getBookingDates());
            listing.setUpdatedAt(LocalDateTime.now());
            listingRepository.save(listing);
        }
        //if booking is accepted change status to accepted
        if (isAccepted) {
            booking.setBookingStatus(BookingStatus.ACCEPTED);
            //if the booking is rejected, add back the booking dates to available dates and change status to rejected
        } else {
            listing.addAvailableDateRange(booking.getBookingDates());
            listing.setUpdatedAt(LocalDateTime.now());
            listingRepository.save(listing);
            booking.setBookingStatus(BookingStatus.REJECTED);
        }
    }

    @Override
    BookingResponse saveOrDeleteListing(String id) {
        //update updateStamp and save booking
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }
}
