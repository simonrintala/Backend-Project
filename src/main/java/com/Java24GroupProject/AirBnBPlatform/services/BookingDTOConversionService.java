package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.NestedListing;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IAuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IIdValidationService;

import java.time.LocalDate;

public class BookingDTOConversionService implements IAuthenticationService, IIdValidationService, PriceCalculationService {


    public BookingDTOConversionService() {
    }

    public BookingResponse convertToDTOResponse(Booking booking) {

        //get listing and user to save variables in DTOResponse
        User user = validateUserIdAndReturnUser(booking.getUser().getId());

        return new BookingResponse(
                booking.getId(),
                booking.getListingInfo(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNr(),
                booking.getBookingDates().getStartDate().toString(),
                booking.getBookingDates().getEndDate().toString(),
                booking.getNumberOfGuests(),
                booking.getTotalPrice(),
                booking.getBookingStatus()
        );
    }

    //convert BookingRequest to Booking
    public Booking convertRequestToBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();

        User currentUser = authenticateAndExtractUser();
        Listing listing = validateListingIdAndReturnListing(bookingRequest.getListingId());

        booking.setListing(listing);
        booking.setListingInfo(new NestedListing(listing.getId(), listing.getTitle(),
                listing.getLocation(),
                listing.getImageUrls().subList(0, 1)));
        //set current user as the user for the booking
        booking.setUser(currentUser);
        booking.setBookingDates(new DateRange(
                LocalDate.parse(bookingRequest.getStartDate()),
                LocalDate.parse(bookingRequest.getEndDate())));
        booking.setNumberOfGuests(bookingRequest.getNumberOfGuests());
        calculateAndSetPrice(booking, listing);
        return booking;
    }
}
