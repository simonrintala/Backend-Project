package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.NestedListing;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;

import java.time.LocalDate;

public class BookingDTOConversionService implements BookingValidationService, AuthenticationService, PriceCalculationService {

    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public BookingDTOConversionService(UserRepository userRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    public BookingResponse convertToDTOResponse(Booking booking) {

        //get listing and user to save variables in DTOResponse
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + booking.getUser().getId() + " not found"));

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

        User currentUser = authenticateAndExtractUser(userRepository);
        Listing listing = validateListingIdAndGetListing(bookingRequest, listingRepository);
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
