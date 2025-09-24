package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectDeleteBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.BookingMethodService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateUpdateBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.IStateHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/************************
 * BookingService
 * ---
 * This class is the interaction point between the BookingController and the different classes
 * coordinating the logic of different booking-related operations.
 * -
 * Logic for the API GET-endpoints is contained directly in the BookingService class, while more complex
 * method (for POST/UPDATE/DELETE/PATCH) are handled via the BookingMethodService class.
 * **********************/

@Service
public class BookingService {

    private final BookingMethodService bookingMethodService;
    private final IdValidationService idValidationService;
    private final UserAuthRepository userAuthRepository;
    private final BookingDTOConversionService bookingDTOConversionService;
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;

    public BookingService(BookingMethodService bookingMethodService, IdValidationService idValidationService, UserAuthRepository userAuthRepository, BookingDTOConversionService bookingDTOConversionService, BookingRepository bookingRepository, ListingRepository listingRepository) {
        this.bookingMethodService = bookingMethodService;
        this.idValidationService = idValidationService;
        this.userAuthRepository = userAuthRepository;
        this.bookingDTOConversionService = bookingDTOConversionService;
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
    }

    //METHODS used by BOOKING CONTROLLER CLASS -----------------------------------------------------------------------
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        CreateUpdateBookingTemplate bookingMethod = (CreateUpdateBookingTemplate) bookingMethodService.getBookingMethod(BookingMethodService.BookingMethodEnum.CREATE);
        return bookingMethod.runMethod(bookingRequest, null);
    }

    public BookingResponse updateBooking(String bookingId, BookingRequest updatedBookingRequest) {
        CreateUpdateBookingTemplate bookingMethod = (CreateUpdateBookingTemplate) bookingMethodService.getBookingMethod(BookingMethodService.BookingMethodEnum.UPDATE);
        return bookingMethod.runMethod(updatedBookingRequest, bookingId);
    }

    public BookingResponse acceptOrRejectBooking(String id, IStateHandler iStateHandler) {
        AcceptRejectDeleteBookingTemplate bookingMethod = (AcceptRejectDeleteBookingTemplate) bookingMethodService.getBookingMethod(BookingMethodService.BookingMethodEnum.ACC_REJ);
        return bookingMethod.runMethod(id, iStateHandler);
    }

    public void deleteBooking(String id) {
        AcceptRejectDeleteBookingTemplate bookingMethod = (AcceptRejectDeleteBookingTemplate) bookingMethodService.getBookingMethod(BookingMethodService.BookingMethodEnum.DELETE);
        bookingMethod.runMethod(id, null);
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

