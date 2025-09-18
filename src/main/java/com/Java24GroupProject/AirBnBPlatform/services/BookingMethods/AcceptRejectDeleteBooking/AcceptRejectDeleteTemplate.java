package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;

import java.time.LocalDateTime;

public abstract class AcceptRejectDeleteTemplate {
    final UserAuthRepository userAuthRepository;
    final IdValidationService idValidationService;
    final ListingRepository listingRepository;
    final BookingRepository bookingRepository;
    Booking booking;
    Listing listing;


    public AcceptRejectDeleteTemplate(UserAuthRepository userAuthRepository, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository) {
        this.userAuthRepository = userAuthRepository;
        this.idValidationService = idValidationService;
        this.listingRepository = listingRepository;
        this.bookingRepository = bookingRepository;
    }

    public final BookingResponse acceptRejectDelete(String bookingId, BookingStatus bookingStatus) {
        setVariables(bookingId, bookingStatus);
        validateOperation();

        if (modifyListingDatesCheck()) {
            listing.addAvailableDateRange(booking.getBookingDates());
            listing.setUpdatedAt(LocalDateTime.now());
            listingRepository.save(listing);
        }
        return updateBooking(bookingId);
    }

    public final void acceptRejectDelete(String bookingId) {
        acceptRejectDelete(bookingId, null);
    }

    void setVariables(String id, BookingStatus bookingStatus) {
        booking = idValidationService.validateBookingIdAndReturnBooking(id);
        listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());
    }
    abstract void validateOperation();
    abstract boolean modifyListingDatesCheck();
    abstract BookingResponse updateBooking(String bookingId);

}
