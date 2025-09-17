package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.NestedListing;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/************************
 * BookingDTOConversionService
 * ---
 * This class contains for mapping data between Booking objects and BookingDTOs (Response- and RequestDTOs)
 * -
 * It is injected by BookingService to handle all conversions of Booking to/from DTOs. This class does not
 * handle data validation of e.g., RequestBodies and is purely an object conversion class.
 ***********************/

@Service
public class BookingDTOConversionService {
    private final AuthenticationService authenticationService;
    private final IdValidationService idValidationService;
    
    public BookingDTOConversionService(AuthenticationService authenticationService, IdValidationService idValidationService) {
        this.authenticationService = authenticationService;
        this.idValidationService = idValidationService;
    }
    
    //convert BookingRequest to Booking
    public Booking convertRequestToBooking(BookingRequest bookingRequest) {
        
        User currentUser = authenticationService.authenticateAndExtractUser();
        Listing listing = idValidationService.validateListingIdAndReturnListing(bookingRequest.getListingId());
        
        Booking booking = new Booking();
        
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
        return booking;
    }
    
    
    //convert Booking object to BookingResponseDTO
    public BookingResponse convertToDTOResponse(Booking booking) {
        
        //get user to save user variables in DTOResponse
        User user = idValidationService.validateUserIdAndReturnUser(booking.getUser().getId());
        
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
    
    //convert list of Booking objects to BookingResponseDTOs
    public List<BookingResponse> convertToDTOResponse(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertToDTOResponse)
                .collect(Collectors.toList());
    }
}