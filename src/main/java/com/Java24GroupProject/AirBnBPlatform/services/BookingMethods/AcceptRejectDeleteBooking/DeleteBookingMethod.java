package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.BookingStateProcessor;

/************************
 * DeleteBookingMethod
 * ---
 * This class contains methods that override abstract methods in the parent class.
 * These methods dictate parts of the process that are different between changing booking status and
 * deleting a booking.
 * **********************/

public class DeleteBookingMethod extends AcceptRejectDeleteBookingTemplate {
    public DeleteBookingMethod(UserAuthRepository userAuthRepository, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository, BookingStateProcessor bookingStateProcessor) {
        super(userAuthRepository, idValidationService, listingRepository, bookingRepository, bookingStateProcessor);
    }

    @Override
    void validateOperation() {
        if (!userAuthRepository.isSameAsCurrentUserOrHasRole(booking.getUser(), Role.ADMIN)) {
            throw new UnauthorizedException("Only the owner of the booking or admin can delete the booking");
        }
    }

    @Override
    void modifyListingDatesViaStatus() {
        //if booking does not have status denied, add back the booked dates to the listing
        if (!booking.getBookingStatus().equals("REJECTED")) {
            super.removeDates();
        }
    }

    @Override
    BookingResponse updateBooking(String bookingId) {
        bookingRepository.deleteById(bookingId);
        return null;
    }
}
