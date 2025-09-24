package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.IBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.BookingStateProcessor;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.IStateHandler;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.RejectState;

/************************
 * AcceptRejectDeleteTemplate
 * ---
 * This class is the abstract parent class to the AcceptRejectBookingMethod and DeleteBookingMethod.
 * This class holds a template method (AcceptRejectDeleteBooking() ) that contains the parts of the accept/reject
 * and delete booking that are the same, and where the different child classes method parts that are different
 * for the accept/reject and delete methods, respectively.
 * ---
 * The classes in the AcceptRejectDeleteBooking-package follow the template method pattern.
 * **********************/

public abstract class AcceptRejectDeleteBookingTemplate implements IBookingMethod {
    final UserAuthRepository userAuthRepository;
    final IdValidationService idValidationService;
    final ListingRepository listingRepository;
    final BookingRepository bookingRepository;
    final BookingStateProcessor bookingStateProcessor;
    Booking booking;
    Listing listing;


    public AcceptRejectDeleteBookingTemplate(UserAuthRepository userAuthRepository, IdValidationService idValidationService, ListingRepository listingRepository, BookingRepository bookingRepository, BookingStateProcessor bookingStateProcessor) {
        this.userAuthRepository = userAuthRepository;
        this.idValidationService = idValidationService;
        this.listingRepository = listingRepository;
        this.bookingRepository = bookingRepository;
        this.bookingStateProcessor = bookingStateProcessor;
    }

    public final BookingResponse runMethod(String bookingId, IStateHandler bookingStatus) {
        //save the booking and listing for the booking in a class variables for ease of use
        setVariables(bookingId, bookingStatus);

        //validate the operation should be allowed to be performed based on e.g., booking status and current user roles
        validateOperation();

        //check if the booking dates should be added back to the listing and if so, add back dates.
        modifyListingDatesViaStatus();

        return updateBooking(bookingId);
    }

    //default methods for setting class variables
    void setVariables(String id, IStateHandler bookingStatus) {
        booking = idValidationService.validateBookingIdAndReturnBooking(id);
        listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());
    }

    void removeDates() {
        bookingStateProcessor.applyState(new RejectState(), booking, listing, listingRepository);
    }

    //abstract methods
    abstract void validateOperation();
    abstract void modifyListingDatesViaStatus();
    abstract BookingResponse updateBooking(String bookingId);

}
