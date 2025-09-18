package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;

public class DeleteBooking extends AcceptRejectDeleteTemplate {
    public DeleteBooking(UserAuthRepository userAuthRepository, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository) {
        super(userAuthRepository, idValidationService, listingRepository, bookingRepository);
    }

    @Override
    void validateOperation() {
        if (!userAuthRepository.isSameAsCurrentUserOrHasRole(booking.getUser(), Role.ADMIN)) {
            throw new UnauthorizedException("Only the owner of the booking or admin can delete the booking");
        }
    }

    @Override
    boolean modifyListingDatesCheck() {
        //if booking does not have status denied, add back the booked dates to the listing
        return  (booking.getBookingStatus() != BookingStatus.REJECTED);
    }

    @Override
    BookingResponse updateBooking(String bookingId) {
        bookingRepository.deleteById(bookingId);
        return null;
    }



}
