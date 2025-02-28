package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
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

    //get booking by id
    public BookingResponse getBookingById(String id) {
        Booking booking = validateBookingIdAndGetBooking(id);

        //convert to DTO
        return convertToDTOResponse(booking);
    }

    public List<BookingResponse> getAllBookings() {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        //convert toDTO
        for (Booking booking : bookingRepository.findAll()) {
            bookingResponses.add(convertToDTOResponse(booking));
        }
        return bookingResponses;
    }

    //get all bookings for a user
    public List<BookingResponse> getBookingByUserId(String userId) {
        //validate user id
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        //convert toDTO
        List<BookingResponse> userBookingResponses = new ArrayList<>();
        for (Booking booking : bookingRepository.findByUser(user)) {
            userBookingResponses.add(convertToDTOResponse(booking));
        }
        return userBookingResponses;
    }

    public BookingResponse updateBooking (String id, BookingRequest updatedBookingRequest) {
        //validate booking id
        Booking booking = validateBookingIdAndGetBooking(id);

        //check if status is pending, otherwise cannot be changed
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Confirmed, payed or cancelled bookings cannot be updated");
        }

        //validate data in new booking
        validateBooking(updatedBookingRequest);
        //convert to booking
        Booking updatedBooking = convertRequestToBooking(updatedBookingRequest);

        //listing of booking cannot be changed
        if (!booking.getListing().getId().equals(updatedBooking.getListing().getId())) {
            throw new IllegalArgumentException("Listing cannot be changed");
        }

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


    public void deleteBooking(String id) {
        //check if id is valid
        Booking booking = validateBookingIdAndGetBooking(id);

        //get listing
        Listing listing = validateListingIdAndGetListing(booking);

        //add back the booked dates to the listing
        listing.addAvailableDateRange(booking.getBookingDates());
        listingRepository.save(listing);

        //delete booking
        bookingRepository.deleteById(id);
    }

    private BookingResponse convertToDTOResponse(Booking booking) {
        //get listing and user to save variables in DTOResponse
        Listing listing = validateListingIdAndGetListing(booking);
        User user = validateUserIdAndGetUser(booking);

        return new BookingResponse(
                listing.getTitle(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNr(),
                booking.getBookingDates().getStartDate().toString(),
                booking.getBookingDates().getEndDate().toString(),
                booking.getNumberOfGuests(),
                booking.getTotalPrice(),
                booking.getBookingStatus(),
                booking.getCreatedAt()
        );
    }

    //convert BookingRequest to Booking
    public Booking convertRequestToBooking(BookingRequest bookingRequest) {
                Booking booking = new Booking();
                booking.setListing(bookingRequest.getListing());
                booking.setUser(bookingRequest.getUser());
                booking.setBookingDates(new DateRange(
                        LocalDate.parse(bookingRequest.getStartDate()),
                        LocalDate.parse(bookingRequest.getEndDate())));
                booking.setNumberOfGuests(bookingRequest.getNumberOfGuests());
                calculateAndSetPrice(booking);
                return booking;
    }

    //validate that BookingRequest data is valid
    private void validateBooking(BookingRequest bookingRequest) {
        //check that user id is valid
        User user = validateUserIdAndGetUser(bookingRequest);

        //check that listing id is valid
        Listing listing = validateListingIdAndGetListing(bookingRequest);

        //check that the user for the booking is not also the host of the listing
        if (user.getId().equals(listing.getHost().getId())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }

        //check that request had dates
        if (bookingRequest.getStartDate() == null || bookingRequest.getEndDate() == null) {
            throw new IllegalArgumentException("booking must have start and end date");
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
        return listingRepository.findById(bookingRequest.getListing().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));

    }

    //validate user id and get user object from booking
    private User validateUserIdAndGetUser(Booking booking) {
        return userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    //validate user id and get user object from bookingRequest
    private User validateUserIdAndGetUser(BookingRequest bookingRequest) {
        return userRepository.findById(bookingRequest.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

}
