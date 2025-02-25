package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    public Booking createBooking(Booking booking) {
        validateBooking(booking);

        booking.CalculateTotalPrice();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedDate(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));
    }
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    public List<Booking> getBookingByUserId(String userId) {
        return bookingRepository.findByUser_Id(userId);
    }
    public Booking updateBooking (String id, Booking updatedBooking) {
        //validate data in new booking
        validateBooking(updatedBooking);

        Booking booking = getBookingById(id);
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new UnsupportedOperationException("Confirmed, payed or cancelled bookings cannot be updated");
        }

        //update booking
        booking.setListing(updatedBooking.getListing());
        booking.setUser(updatedBooking.getUser());
        booking.setNumberOfGuests(updatedBooking.getNumberOfGuests());
        booking.setBookingDates(updatedBooking.getBookingDates());

        if (!booking.getBookingDates().getStartDate().equals(updatedBooking.getBookingDates().getStartDate()) ||
                !booking.getBookingDates().getEndDate().equals(updatedBooking.getBookingDates().getEndDate())) {
            booking.setTotalPrice(calculatePrice(updatedBooking));
        }

        return bookingRepository.save(booking);
    }


    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }

    public BookingResponse convertToDTO(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getListing().getTitle(),
                booking.getListing().getHost().getUsername(),
                booking.getUser().getUsername(),
                booking.getUser().getEmail(),
                booking.getUser().getPhoneNr(),
                booking.getBookingDates().getStartDate(),
                booking.getBookingDates().getEndDate(),
                booking.getNumberOfGuests(),
                booking.getTotalPrice()

        );
    }

    public BookingRequest convertToDTORequest(Booking booking) {
        return new BookingRequest(
                booking.getId(),
                booking.getListing().getTitle(),
                booking.getListing().getHost().getUsername(),
                booking.getBookingDates().getStartDate(),
                booking.getBookingDates().getEndDate(),
                booking.getNumberOfGuests(),
                booking.getTotalPrice()
        );
    }

    private void validateBooking(Booking booking) {
        //check that user id is valid
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("user id not found"));

        //check that listing id is valid
        Listing listing = listingRepository.findById(booking.getListing().getId())
                .orElseThrow(() -> new ResourceNotFoundException("listing id not found"));

        //check that the user for the booking is not also the host of the listing
        if (user.getId().equals(listing.getHost().getId())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }

        //check that nrOfGuest does not exceed listing capacity
        if (booking.getNumberOfGuests() > listing.getCapacity()) {
            throw new IllegalArgumentException("nrOfGuest on the booking exceeds listing capacity");
        }



    }


    private BigDecimal calculatePrice(Booking booking) {
        long daysBetween = ChronoUnit.DAYS.between(
                booking.getBookingDates().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                booking.getBookingDates().getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        return booking.getListing().getPrice_per_night().multiply(BigDecimal.valueOf(daysBetween));
    }

}
