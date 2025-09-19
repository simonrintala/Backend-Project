package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies.PriceContext;
import com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies.StandardStrategy;

/************************
 * CreateUpdateBookingTemplate
 * ---
 * This class is the abstract parent class to the CreateBookingMethod and UpdateBookingMethod.
 * This class holds a template method (CreateUpdateBooking() ) that contains the parts of the create
 * and update booking that are the same and where the different child classes override the parts to the
 * method that are create or update specific, respectively.
 * ---
 * The classes in the CreateUpdateBooking-package follow the template method pattern.
 * **********************/

public abstract class CreateUpdateBookingTemplate {
    final IdValidationService idValidationService;
    final UserAuthRepository userAuthRepository;
    final BookingDTOConversionService bookingDTOConversionService;
    final BookingRepository bookingRepository;
    final DateAvailabilityService dateAvailabilityService;
    final PriceContext priceContext;
    Listing listing;
    Booking booking;


    public CreateUpdateBookingTemplate(IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService) {
        this.idValidationService = idValidationService;
        this.userAuthRepository = userAuthRepository;
        this.bookingDTOConversionService = bookingDTOConversionService;
        this.bookingRepository = bookingRepository;
        this.dateAvailabilityService = dateAvailabilityService;
        priceContext = new PriceContext(new StandardStrategy());
    }

    public final BookingResponse createUpdateBooking(BookingRequest bookingRequest, String bookingId) {
        //save the listing specified in the booking request in a class variable for ease of use
        setVariables(bookingRequest, bookingId);

        //validate the data in the booking request
        validateBookingRequest(bookingRequest);

        //convert from RequestDTO to Booking and save in a class variable
        convertRequestDTOtoBooking(bookingRequest);

        //validate that booking dates are available, and if so, update listing dates and set booking total price
        updateListingDatesAndSetPrice();

        //set remaining fields for the booking (e.g., bookingStatus)
        setRemainingFields();

        //save new/updated booking
        bookingRepository.save(booking);

        //return as ResponseDTO
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    //default setModels method, overridden by UpdateBookingMethods by method that also calls the super-method
    void setVariables(BookingRequest bookingRequest, String bookingId) {
        listing = idValidationService.validateListingIdAndReturnListing(bookingRequest.getListingId());
    }


    //default setModels method, overridden by UpdateBookingMethods by method that also calls the super-method
    void validateBookingRequest(BookingRequest bookingRequest) {
        if (userAuthRepository.isSameAsCurrentUser(listing.getHost())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }
        //check that nrOfGuest does not exceed listing capacity
        if (bookingRequest.getNumberOfGuests() > listing.getCapacity()) {
            throw new IllegalArgumentException("nrOfGuest on the booking exceeds listing capacity");
        }
    }

    //abstract methods
    abstract void convertRequestDTOtoBooking(BookingRequest bookingRequest);
    abstract void updateListingDatesAndSetPrice();
    abstract void setRemainingFields();

}


