package com.Java24GroupProject.AirBnBPlatform.services.BookingMethodTemplates;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import com.Java24GroupProject.AirBnBPlatform.services.PriceCalculationService;

public abstract class BookingCreateUpdateTemplate {
    final IdValidationService idValidationService;
    final AuthenticationService authenticationService;
    final BookingDTOConversionService bookingDTOConversionService;
    final BookingRepository bookingRepository;
    final DateAvailabilityService dateAvailabilityService;
    final PriceCalculationService priceCalculationService;
    Listing listing;
    Booking booking;

    public BookingCreateUpdateTemplate(IdValidationService idValidationService, AuthenticationService authenticationService, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, DateAvailabilityService dateAvailabilityService, PriceCalculationService priceCalculationService) {
        this.idValidationService = idValidationService;
        this.authenticationService = authenticationService;
        this.bookingDTOConversionService = bookingDTOConversionService;
        this.bookingRepository = bookingRepository;
        this.dateAvailabilityService = dateAvailabilityService;
        this.priceCalculationService = priceCalculationService;
    }

    public BookingResponse createUpdateBooking(BookingRequest bookingRequest, String bookingId) {

        setModels(bookingRequest, bookingId);

        validateBookingRequest(bookingRequest);

        //convert from RequestDTO to Booking
        convertRequestDTOtoBooking(bookingRequest);
        Booking booking = bookingDTOConversionService.convertRequestToBooking(bookingRequest);

        //validate that booking dates are available and update listing dates, set status and price
        updateListingDatesAndSetPrice();

        setRemainingFields();

        //save booking
        bookingRepository.save(booking);

        //return as DTO
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    void setModels(BookingRequest bookingRequest, String bookingId) {
        listing = idValidationService.validateListingIdAndReturnListing(bookingRequest.getListingId());
    }

    void validateBookingRequest(BookingRequest bookingRequest) {
        if (!authenticationService.isSameAsCurrentUser(listing.getHost())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }
        //check that nrOfGuest does not exceed listing capacity
        if (bookingRequest.getNumberOfGuests() > listing.getCapacity()) {
            throw new IllegalArgumentException("nrOfGuest on the booking exceeds listing capacity");
        }
    }

    abstract void convertRequestDTOtoBooking(BookingRequest bookingRequest);
    abstract void updateListingDatesAndSetPrice();
    abstract void setRemainingFields();

}


