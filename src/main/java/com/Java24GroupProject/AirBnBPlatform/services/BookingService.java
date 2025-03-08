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
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
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
        validateBooking(bookingRequest);

        //convert from RequestDTO to Booking
        Booking booking = convertRequestToBooking(bookingRequest);

        //validate that booking dates are available and update listing dates
        validateBookingDatesAndUpdateListing(booking);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setUpdatedAt(null);

        //save booking
        bookingRepository.save(booking);

        //return as DTO
        return convertToDTOResponse(booking);
    }

    //get bookings by id
    public BookingResponse getBookingById(String id) {
        Booking booking = validateBookingIdAndGetBooking(id);

        //convert to DTO
        return convertToDTOResponse(booking);
    }

    //get all bookings
    public List<BookingResponse> getAllBookings() {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        //convert toDTO
        for (Booking booking : bookingRepository.findAll()) {
            bookingResponses.add(convertToDTOResponse(booking));
        }
        return bookingResponses;
    }

    //get bookings any user
    public List<BookingResponse> getBookingsByUserId(String userId) {
        //validate user id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return getUserBookings(user);
    }

    //get bookings current user
    public List<BookingResponse> getBookingsCurrentUser() {
        //get current user
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        return getUserBookings(currentUser);
    }

    public List<BookingResponse> getBookingsByListingId(String listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(()-> new ResourceNotFoundException("Listing with id " + listingId + "not found"));

        //check that current user is owner of listing or admin
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        if (!currentUser.getId().equals(listing.getHost().getId()) && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new UnauthorizedException("Only the listing host and admin can see all bookings for a listing");
        }

        //convert toDTO
        List<BookingResponse> userBookingResponses = new ArrayList<>();
        for (Booking booking : bookingRepository.findByListing(listing)) {
            userBookingResponses.add(convertToDTOResponse(booking));
        }
        return userBookingResponses;
    }

    public BookingResponse updateBooking(String id, BookingRequest updatedBookingRequest) {
        //validate booking id
        Booking booking = validateBookingIdAndGetBooking(id);

        //check that current user is owner of booking
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        if (!currentUser.getId().equals(booking.getUser().getId())) {
            throw new UnauthorizedException("Only the owner of the booking can update the booking");
        }

        //check if status is pending, otherwise cannot be changed
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Confirmed or denied bookings cannot be updated");
        }

        //listing of booking cannot be changed
        if (!booking.getListing().getId().equals(updatedBookingRequest.getListingId())) {
            throw new IllegalArgumentException("Listing cannot be changed");
        }

        //validate data in new booking
        validateBooking(updatedBookingRequest);

        //convert DTO to booking object
        Booking updatedBooking = convertRequestToBooking(updatedBookingRequest);

        //if booking dates are changed
        if (!booking.getBookingDates().getStartDate().equals(updatedBooking.getBookingDates().getStartDate()) ||
                !booking.getBookingDates().getEndDate().equals(updatedBooking.getBookingDates().getEndDate())) {

            //add back the old dates
            Listing listing = validateListingIdAndGetListing(booking);
            listing.addAvailableDateRange(booking.getBookingDates());
            listingRepository.save(listing);

            //subtract new dates from listing
            validateBookingDatesAndUpdateListing(updatedBooking);
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
        Booking booking = validateBookingIdAndGetBooking(id);

        //check that booking status is pending
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Confirmed or denied bookings cannot be updated");
        }

        //get current logged-in user
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);

        //get listing for the booking (to check that the current user is the host of the listing)
        Listing listing = validateListingIdAndGetListing(booking);

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
        Booking booking = validateBookingIdAndGetBooking(id);

        //check that current user is owner of booking or admin
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        if (!currentUser.getId().equals(booking.getUser().getId()) && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new UnauthorizedException("Only the owner of the booking or admin can delete the booking");
        }

        //get listing
        Listing listing = validateListingIdAndGetListing(booking);

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

    //get bookings current user
    private List<BookingResponse> getUserBookings(User user) {
        //convert toDTO
        List<BookingResponse> userBookingResponses = new ArrayList<>();
        for (Booking booking : bookingRepository.findByUser(user)) {
            userBookingResponses.add(convertToDTOResponse(booking));
        }
        return userBookingResponses;
    }

    private BookingResponse convertToDTOResponse(Booking booking) {
        //get listing and user to save variables in DTOResponse
        Listing listing = validateListingIdAndGetListing(booking);
        User user = validateUserIdAndGetUser(booking);

        return new BookingResponse(
                booking.getId(),
                listing.getTitle(),
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
                booking.setListing(validateListingIdAndGetListing(bookingRequest));
                //set current user as the user for the booking
                booking.setUser(UserService.verifyAuthenticationAndExtractUser(userRepository));
                booking.setBookingDates(new DateRange(
                        LocalDate.parse(bookingRequest.getStartDate()),
                        LocalDate.parse(bookingRequest.getEndDate())));
                booking.setNumberOfGuests(bookingRequest.getNumberOfGuests());
                calculateAndSetPrice(booking);
                return booking;
    }

    //calculate price from nr of booked days and price per night from listing
    private void calculateAndSetPrice(Booking booking) {
        //calculate days in between start and end date
        long daysBetween = ChronoUnit.DAYS.between(
                booking.getBookingDates().getStartDate(),
                booking.getBookingDates().getEndDate()
        );

        //get listing
        Listing listing = validateListingIdAndGetListing(booking);

        //calculate price using listing price_per_night
        BigDecimal totalPrice = listing.getPricePerNight().multiply(BigDecimal.valueOf(daysBetween));

        //set total price of booking
        booking.setTotalPrice(totalPrice);
    }

    //validate that BookingRequest data is valid
    private void validateBooking(BookingRequest bookingRequest) {
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);

        //check that listing id is valid
        Listing listing = validateListingIdAndGetListing(bookingRequest);

        //check that the user for the booking is not also the host of the listing
        if (currentUser.getId().equals(listing.getHost().getId())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }

        //check that nrOfGuest does not exceed listing capacity
        if (bookingRequest.getNumberOfGuests() > listing.getCapacity()) {
            throw new IllegalArgumentException("nrOfGuest on the booking exceeds listing capacity");
        }

    }

    //validate that booking dates are available and update listing dates
    private void validateBookingDatesAndUpdateListing(Booking booking) {
        //get listing
        Listing listing = validateListingIdAndGetListing(booking);

        //save booking dates in variable for ease of use
        DateRange bookingDates = booking.getBookingDates();

        //check that booking dates fall within available dates of listing.
        boolean areBookingDatesAvailable = false;
        for (DateRange availibleDateRange : listing.getAvailableDates()) {
            if (bookingDates.isWithinAnotherDateRange(availibleDateRange)) {
                areBookingDatesAvailable = true;

                //update listing dates
                if (bookingDates.isIdenticalToAnotherDateRange(availibleDateRange)) {
                    listing.getAvailableDates().remove(availibleDateRange);
                } else if (bookingDates.getStartDate().isEqual(availibleDateRange.getStartDate())) {
                    availibleDateRange.setStartDate(bookingDates.getEndDate());
                } else if (bookingDates.getEndDate().isEqual(availibleDateRange.getEndDate())) {
                    availibleDateRange.setEndDate(bookingDates.getStartDate());
                } else {
                    DateRange newDateRange = new DateRange(bookingDates.getEndDate(), availibleDateRange.getEndDate());
                    listing.getAvailableDates().add(newDateRange);
                    availibleDateRange.setEndDate(bookingDates.getStartDate());
                }

                //save updated listing
                listing.setUpdatedAt(LocalDateTime.now());
                listingRepository.save(listing);
                break;
            }
        }

        //error if bookingDates are not available in listing
        if(!areBookingDatesAvailable) {
            throw new IllegalArgumentException("booking dates not available on listing");
        }
    }

    //validate id and get booking object
    private Booking validateBookingIdAndGetBooking(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    //validate listing id and get listing object from booking
    private Listing validateListingIdAndGetListing(Booking booking) {
        return listingRepository.findById(booking.getListing().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
    }

    //validate listing id and get listing object from bookingRequest
    private Listing validateListingIdAndGetListing(BookingRequest bookingRequest) {
        return listingRepository.findById(bookingRequest.getListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));

    }

    //validate user id and get user object from booking
    private User validateUserIdAndGetUser(Booking booking) {
        return userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

}
