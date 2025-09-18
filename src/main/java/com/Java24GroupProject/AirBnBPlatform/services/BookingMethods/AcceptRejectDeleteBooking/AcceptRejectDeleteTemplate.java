package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;

public abstract class AcceptRejectDeleteTemplate {
    final AuthenticationService authenticationService;
    final IdValidationService idValidationService;
    final ListingRepository listingRepository;
    final BookingRepository bookingRepository;
    Booking booking;
    Listing listing;


    public AcceptRejectDeleteTemplate(AuthenticationService authenticationService, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository) {
        this.authenticationService = authenticationService;
        this.idValidationService = idValidationService;
        this.listingRepository = listingRepository;
        this.bookingRepository = bookingRepository;
    }

    public final BookingResponse acceptRejectDelete(String id, Boolean isAccepted) {
        setVariables(id, isAccepted);
        validateOperation();
        modifyListingDates();
        return saveOrDeleteListing(id);
    }

    void setVariables(String id, boolean isAccepted) {
        booking = idValidationService.validateBookingIdAndReturnBooking(id);
        listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());
    }
    abstract void validateOperation();
    abstract void modifyListingDates();
    abstract BookingResponse saveOrDeleteListing(String id);

}
