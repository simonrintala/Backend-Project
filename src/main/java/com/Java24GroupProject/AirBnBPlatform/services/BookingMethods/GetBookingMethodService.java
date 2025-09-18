package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetBookingMethodService {
    final IdValidationService idValidationService;
    final UserAuthRepository userAuthRepository;
    final BookingDTOConversionService bookingDTOConversionService;
    final BookingRepository bookingRepository;
    final ListingRepository listingRepository;

    public GetBookingMethodService(ListingRepository listingRepository, IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository) {
        this.idValidationService = idValidationService;
        this.userAuthRepository = userAuthRepository;
        this.bookingDTOConversionService = bookingDTOConversionService;
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
    }

    //get bookings by id
    public BookingResponse getBookingById(String id) {
        Booking booking = idValidationService.validateBookingIdAndReturnBooking(id);
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    //get all bookings
    public List<BookingResponse> getAllBookings() {
        //check that current user is admin
        if (!userAuthRepository.doesCurrentUserHaveThisRole(Role.ADMIN)) {
            throw new UnauthorizedException("Only admin can see all bookings");
        }
        List<Booking> bookings = bookingRepository.findAll();
        return bookingDTOConversionService.convertToDTOResponse(bookings);
    }

    //get bookings any user
    public List<BookingResponse> getBookingsByUserId(String userId) {

        //check that current user is admin
        if (!userAuthRepository.doesCurrentUserHaveThisRole(Role.ADMIN)) {
            throw new UnauthorizedException("Only admin can see all bookings for another user");
        }

        List<Booking> bookings = bookingRepository.findByUser(
                idValidationService.validateUserIdAndReturnUser(userId));
        return bookingDTOConversionService.convertToDTOResponse(bookings);
    }

    //get bookings current user
    public List<BookingResponse> getBookingsCurrentUser() {
        List<Booking> bookings = bookingRepository.findByUser(
                userAuthRepository.authenticateAndExtractUser());
        return bookingDTOConversionService.convertToDTOResponse(bookings);    }

    //get all bookings for current user's listings
    public List<BookingResponse> getBookingsForListingsOfCurrentUser() {
        User currentUser = userAuthRepository.authenticateAndExtractUser();
        List<Listing> userListings = listingRepository.findByHost(currentUser);

        List<Booking> bookingsForCurrentUserListings = new ArrayList<>();
        for (Listing listing : userListings) {
            bookingsForCurrentUserListings.addAll(bookingRepository.findByListing(listing));
        }
        return bookingDTOConversionService.convertToDTOResponse(bookingsForCurrentUserListings);
    }

    //get all bookings for a listing using listingId
    public List<BookingResponse> getBookingsByListingId(String listingId) {
        Listing listing = idValidationService.validateListingIdAndReturnListing(listingId);

        //check that current user is owner of listing or admin
        if (!userAuthRepository.isSameAsCurrentUserOrHasRole(listing.getHost(), Role.ADMIN)) {
            throw new UnauthorizedException("Only the listing host and admin can see all bookings for a listing");
        }

        //convert to DTO and return
        List<Booking> bookings = bookingRepository.findByListing(listing);
        return bookingDTOConversionService.convertToDTOResponse(bookings);
    }
}
