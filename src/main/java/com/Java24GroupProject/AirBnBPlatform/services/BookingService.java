package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
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
import java.util.Set;

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
        Listing listing = listingRepository.findById(booking.getListing().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));

        //Pris calc
        Long daysBooked = ChronoUnit.DAYS.between(
                booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                booking.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        //ändra getPrice_per_night till korrekt variabel
        BigDecimal totalPrice = listing.getPrice_Per_Night().multiply(BigDecimal.valueOf(daysBooked));
        booking.setTotalPrice(totalPrice);

        //lägg till status
        booking.setBookingStatus(Set.of(BookingStatus.PENDING));
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
        Booking booking = getBookingById(id);

        booking.setStartDate(updatedBooking.getStartDate());
        booking.setEndDate(updatedBooking.getEndDate());
        booking.setNumberOfGuests(updatedBooking.getNumberOfGuests());

        // Om datum ändrats, beräkna nytt totalpris
        if (!booking.getStartDate().equals(updatedBooking.getStartDate()) ||
                !booking.getEndDate().equals(updatedBooking.getEndDate())) {

            long daysBetween = ChronoUnit.DAYS.between(
                    booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    booking.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );

            //ändra getPrice_per_night till korrekt variabel
            BigDecimal newTotalPrice = booking.getListing().getPrice_Per_Night().multiply(BigDecimal.valueOf(daysBetween));
            booking.setTotalPrice(newTotalPrice);
        }
        return bookingRepository.save(booking);
    }
    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }


}
