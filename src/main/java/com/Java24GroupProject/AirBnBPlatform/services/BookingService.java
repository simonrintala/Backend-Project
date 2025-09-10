package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.NestedListing;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService implements BookingValidationService, AuthenticationService, PriceCalculationService, DateAvailabilityService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ListingRepository listingRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    //METHODS used by BOOKING CONTROLLER CLASS -----------------------------------------------------------------------

    public BookingResponse createBooking(BookingRequest bookingRequest) {
        //validate that bookingRequest data is valid
        User currentUser = authenticateAndExtractUser(userRepository);
        validateBooking(bookingRequest, currentUser, listingRepository);

        //convert from RequestDTO to Booking
        Booking booking = convertRequestToBooking(bookingRequest);

        //validate that booking dates are available and update listing dates
        Listing listing = validateListingIdAndGetListing(booking, listingRepository);
        validateBookingDatesAndUpdateListing(booking, listing, listingRepository);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setUpdatedAt(null);

        //save booking
        bookingRepository.save(booking);

        //return as DTO
        return convertToDTOResponse(booking);
    }

    //get bookings by id
    public BookingResponse getBookingById(String id) {
        Booking booking = validateBookingIdAndGetBooking(id, bookingRepository);

        //convert to DTO
        return convertToDTOResponse(booking);
    }

    //get all bookings
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::convertToDTOResponse)
                .collect(Collectors.toList());
    }

    //get bookings any user
    public List<BookingResponse> getBookingsByUserId(String userId) {
        //validate user id
        User user = UserService.validateUserIdAndReturnUser(userId, userRepository);

        return getUserBookings(user);
    }

    //get bookings current user
    public List<BookingResponse> getBookingsCurrentUser() {
        //get current user
        User currentUser = authenticateAndExtractUser(userRepository);
        return getUserBookings(currentUser);
    }

    //get all bookings for current user's listings
    public List<BookingResponse> getListingBookingsCurrentUser() {
        User currentUser = authenticateAndExtractUser(userRepository);
        List<Listing> userListings = listingRepository.findByHost(currentUser);
        List<BookingResponse> listingBookingsCurrentUser = new ArrayList<>();

        for (Listing listing : userListings) {
            listingBookingsCurrentUser.addAll(getBookingsByListingId(listing.getId()));
        }

        return listingBookingsCurrentUser;

    }

    //get current listings bookingId
    public List<BookingResponse> getBookingsByListingId(String listingId) {
        Listing listing = ListingService.validateListingIdAndGetListing(listingId, listingRepository);
        //check that current user is owner of listing or admin
        User currentUser = authenticateAndExtractUser(userRepository);
        if (!currentUser.getId().equals(listing.getHost().getId()) && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new UnauthorizedException("Only the listing host and admin can see all bookings for a listing");
        }

        //convert toDTO and return
        List<Booking> bookings = bookingRepository.findByListing(listing);
        return bookings.stream()
                .map(this::convertToDTOResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse updateBooking(String id, BookingRequest updatedBookingRequest) {
        //validate booking id
        Booking booking = validateBookingIdAndGetBooking(id, bookingRepository);

        //check that current user is owner of booking
        User currentUser = authenticateAndExtractUser(userRepository);
        if (!currentUser.getId().equals(booking.getUser().getId())) {
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
        validateBooking(updatedBookingRequest, currentUser, listingRepository);

        //convert DTO to booking object
        Booking updatedBooking = convertRequestToBooking(updatedBookingRequest);

        //if booking dates are changed
        if (!booking.getBookingDates().getStartDate().equals(updatedBooking.getBookingDates().getStartDate()) ||
                !booking.getBookingDates().getEndDate().equals(updatedBooking.getBookingDates().getEndDate())) {

            //add back the old dates
            Listing listing = validateListingIdAndGetListing(booking, listingRepository);
            listing.addAvailableDateRange(booking.getBookingDates());
            listingRepository.save(listing);

            //subtract new dates from listing
            validateBookingDatesAndUpdateListing(updatedBooking, listing, listingRepository);
            booking.setBookingDates(updatedBooking.getBookingDates());
        }

        //update other booking data booking
        booking.setNumberOfGuests(updatedBooking.getNumberOfGuests());

        //update updatedAt
        booking.setUpdatedAt(LocalDateTime.now());

        //save booking
        bookingRepository.save(booking);

        //return as DTO
        return convertToDTOResponse(booking);
    }

    public BookingResponse acceptOrRejectBooking(String id, boolean isAccepted) {
        //get booking from repository
        Booking booking = validateBookingIdAndGetBooking(id, bookingRepository);

        //check that booking status is pending
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Booking has already been accepted or rejected");
        }

        //get current logged-in user
        User currentUser = authenticateAndExtractUser(userRepository);

        //get listing for the booking (to check that the current user is the host of the listing)
        Listing listing = validateListingIdAndGetListing(booking, listingRepository);

        //check that current user is the host of the listing the booking refers to, otherwise cast error
        if (!listing.getHost().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("only the listing host can accept/reject a booking");
        }

        //if booking is accepted change status to accepted
        if (isAccepted) {
            booking.setBookingStatus(BookingStatus.ACCEPTED);
        //if the booking is rejected, add back the booking dates to available dates and change status to rejected
        } else {
            listing.addAvailableDateRange(booking.getBookingDates());
            listing.setUpdatedAt(LocalDateTime.now());
            listingRepository.save(listing);
            booking.setBookingStatus(BookingStatus.REJECTED);
        }

        //save updated booking
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return convertToDTOResponse(booking);
    }

    public void deleteBooking(String id) {
        //check if id is valid
        Booking booking = validateBookingIdAndGetBooking(id, bookingRepository);

        //check that current user is owner of booking or admin
        User currentUser = authenticateAndExtractUser(userRepository);
        if (!currentUser.getId().equals(booking.getUser().getId()) && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new UnauthorizedException("Only the owner of the booking or admin can delete the booking");
        }

        //get listing
        Listing listing = validateListingIdAndGetListing(booking, listingRepository);

        //if booking does not have status denied, add back the booked dates to the listing
        if(booking.getBookingStatus() != BookingStatus.REJECTED) {
            listing.addAvailableDateRange(booking.getBookingDates());
            listing.setUpdatedAt(LocalDateTime.now());
            listingRepository.save(listing);
        }

        //delete booking
        bookingRepository.deleteById(id);
    }


    //METHODS used by this or other SERVICE CLASSES --------------------------------------------------------------

    //get bookings for a user, used by getBookingsByUserId and getBookingsCurrentUser methods
    private List<BookingResponse> getUserBookings(User user) {

        //convert toDTO and return
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .map(this::convertToDTOResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse convertToDTOResponse(Booking booking) {
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
    private Booking convertRequestToBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();

        Listing listing = validateListingIdAndGetListing(bookingRequest, listingRepository);
        booking.setListing(listing);
        booking.setListingInfo(new NestedListing(listing.getId(), listing.getTitle(),
                listing.getLocation(),
                listing.getImageUrls().subList(0, 1)));
        //set current user as the user for the booking
        booking.setUser(authenticateAndExtractUser(userRepository));
        booking.setBookingDates(new DateRange(
                LocalDate.parse(bookingRequest.getStartDate()),
                LocalDate.parse(bookingRequest.getEndDate())));
        booking.setNumberOfGuests(bookingRequest.getNumberOfGuests());
        calculateAndSetPrice(booking, listing);
        return booking;
    }


}
