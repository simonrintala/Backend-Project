package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;

import java.time.LocalDateTime;

public class DeleteBooking extends AcceptRejectDeleteTemplate {
    public DeleteBooking(AuthenticationService authenticationService, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository) {
        super(authenticationService, idValidationService, listingRepository, bookingRepository);
    }

    @Override
    void validateOperation() {
        if (!authenticationService.isSameAsCurrentUserOrHasRole(booking.getUser(), Role.ADMIN)) {
            throw new UnauthorizedException("Only the owner of the booking or admin can delete the booking");
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
    }

    @Override
    BookingResponse saveOrDeleteListing(String id) {
        //delete booking
        bookingRepository.deleteById(id);
        return null;
    }

}
