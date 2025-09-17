package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.AuthenticationService;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.AcceptState;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.IStateHandler;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.PendingState;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.RejectState;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
public class BookingService implements BookingValidationService, PriceCalculationService, DateAvailabilityService {
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    private final BookingDTOConversionService bookingDTOConversionService;
    private final AuthenticationService authenticationService;
    private final IdValidationService idValidationService;

    public BookingService(BookingDTOConversionService bookingDTOConversionService, AuthenticationService authenticationService, IdValidationService idValidationService, BookingRepository bookingRepository, ListingRepository listingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
        this.bookingDTOConversionService = bookingDTOConversionService;
        this.authenticationService = authenticationService;
        this.idValidationService = idValidationService;
    }

    //METHODS used by BOOKING CONTROLLER CLASS -----------------------------------------------------------------------

    public BookingResponse createBooking(BookingRequest bookingRequest) {
        //validate that bookingRequest data is valid
        User currentUser = authenticationService.authenticateAndExtractUser();
        Listing listing = idValidationService.validateListingIdAndReturnListing(bookingRequest.getListingId());
        validateBooking(bookingRequest, currentUser, listing);

        //convert from RequestDTO to Booking
        Booking booking = bookingDTOConversionService.convertRequestToBooking(bookingRequest);

        //validate that booking dates are available and update listing dates, set status and price
        validateBookingDatesAndUpdateListing(booking, listing, listingRepository);
        calculateAndSetPrice(booking, listing);
        IStateHandler pending = new PendingState();
        pending.apply(booking, listing, listingRepository);
        booking.setUpdatedAt(null);

        //save booking
        bookingRepository.save(booking);

        //return as DTO
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    //get bookings by id
    public BookingResponse getBookingById(String id) {
        Booking booking = idValidationService.validateBookingIdAndReturnBooking(id);
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    //get all bookings
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookingDTOConversionService.convertToDTOResponse(bookings);
    }

    //get bookings any user
    public List<BookingResponse> getBookingsByUserId(String userId) {
        List<Booking> bookings = bookingRepository.findByUser(
                idValidationService.validateUserIdAndReturnUser(userId));
        return bookingDTOConversionService.convertToDTOResponse(bookings);
    }

    //get bookings current user
    public List<BookingResponse> getBookingsCurrentUser() {
        List<Booking> bookings = bookingRepository.findByUser(
                authenticationService.authenticateAndExtractUser());
        return bookingDTOConversionService.convertToDTOResponse(bookings);    }

    //get all bookings for current user's listings
    public List<BookingResponse> getListingBookingsCurrentUser() {
        User currentUser = authenticationService.authenticateAndExtractUser();
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
        if (!authenticationService.isSameAsCurrentUserOrHasRole(listing.getHost(), Role.ADMIN)) {
            throw new UnauthorizedException("Only the listing host and admin can see all bookings for a listing");
        }

        //convert to DTO and return
        List<Booking> bookings = bookingRepository.findByListing(listing);
        return bookingDTOConversionService.convertToDTOResponse(bookings);
    }

    public BookingResponse updateBooking(String id, BookingRequest updatedBookingRequest) {
        //validate booking and listing id
        Booking booking = idValidationService.validateBookingIdAndReturnBooking(id);
        Listing listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());

        //check that current user is owner of booking
        User currentUser = authenticationService.authenticateAndExtractUser();
        if (!authenticationService.isSameAsCurrentUser(booking.getUser())) {
            throw new UnauthorizedException("Only the owner of the booking can update the booking");
        }

        //check if status is pending, otherwise cannot be changed
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Accepted or rejected bookings cannot be updated");
        }

        //listing of booking cannot be changed
        if (!booking.getListing().getId().equals(updatedBookingRequest.getListingId())) {
            throw new IllegalArgumentException("Listing cannot be changed");
        }

        //validate data in new booking
        validateBooking(updatedBookingRequest, currentUser, listing);

        //convert DTO to booking object
        Booking updatedBooking = bookingDTOConversionService.convertRequestToBooking(updatedBookingRequest);

        //if booking dates are changed
        if (!booking.getBookingDates().getStartDate().equals(updatedBooking.getBookingDates().getStartDate()) ||
                !booking.getBookingDates().getEndDate().equals(updatedBooking.getBookingDates().getEndDate())) {

            //add back the old dates
            listing.addAvailableDateRange(booking.getBookingDates());
            listingRepository.save(listing);

            //subtract new dates from listing
            validateBookingDatesAndUpdateListing(updatedBooking, listing, listingRepository);
            booking.setBookingDates(updatedBooking.getBookingDates());
            calculateAndSetPrice(booking, listing);
        }

        //update other booking data booking
        booking.setNumberOfGuests(updatedBooking.getNumberOfGuests());

        //update updatedAt
        booking.setUpdatedAt(LocalDateTime.now());

        //save booking
        bookingRepository.save(booking);

        //return as DTO
        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    public BookingResponse acceptOrRejectBooking(String id, boolean isAccepted) {
        //get booking from repository
        Booking booking = idValidationService.validateBookingIdAndReturnBooking(id);

        //check that booking status is pending
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Booking has already been accepted or rejected");
        }

        //get current logged-in user
        User currentUser = authenticationService.authenticateAndExtractUser();

        //get listing for the booking (to check that the current user is the host of the listing)
        Listing listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());

        //check that current user is the host of the listing the booking refers to, otherwise cast error
        if (!authenticationService.isSameAsCurrentUser(listing.getHost())) {
            throw new UnauthorizedException("only the listing host can accept/reject a booking");
        }

        //if booking is accepted change status to accepted
        if (isAccepted) {
            IStateHandler accept = new AcceptState();
            accept.apply(booking, listing, listingRepository);
        } else {
            IStateHandler reject = new RejectState();
            reject.apply(booking, listing, listingRepository);
        }

        //save updated booking
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return bookingDTOConversionService.convertToDTOResponse(booking);
    }

    public void deleteBooking(String id) {
        //check if id is valid
        Booking booking = idValidationService.validateBookingIdAndReturnBooking(id);

        //check that current user is owner of booking or admin
        if (!authenticationService.isSameAsCurrentUserOrHasRole(booking.getUser(), Role.ADMIN)) {
            throw new UnauthorizedException("Only the owner of the booking or admin can delete the booking");
        }

        //get listing
        Listing listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());

        //if booking does not have status denied, add back the booked dates to the listing
        if(booking.getBookingStatus() != BookingStatus.REJECTED) {
            // If not previously rejected, release dates on delete as a common reset
            listing.addAvailableDateRange(booking.getBookingDates());
            listing.setUpdatedAt(LocalDateTime.now());
            listingRepository.save(listing);
        }

        //delete booking
        bookingRepository.deleteById(id);
    }
}
