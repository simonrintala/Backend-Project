package com.Java24GroupProject.AirBnBPlatform.controllers;


import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'HOST', 'ADMIN')")
    public ResponseEntity<BookingRequest> createBooking(@Valid @RequestBody Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        BookingRequest request = bookingService.convertToDTORequest(newBooking);
        return new ResponseEntity<>(request, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = bookings.stream()
                .map(bookingService::convertToDTO)
                .toList();
        return new ResponseEntity<>(bookingResponses, HttpStatus.OK);
        //return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String id) {
        Booking booking = bookingService.getBookingById(id);
        BookingResponse bookingResponse = bookingService.convertToDTO(booking);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        //return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BookingResponse> getBookingsByUserId(@PathVariable String userId) {
        List<Booking> bookings = bookingService.getBookingByUserId(userId);
        List<BookingResponse> bookingResponses = bookings.stream()
                .map(bookingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity(bookingResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable String id, @Valid @RequestBody Booking updatedBooking) {
        Booking updated = bookingService.updateBooking(id, updatedBooking);
        BookingResponse bookingResponse = bookingService.convertToDTO(updated);
        return new ResponseEntity(bookingResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }



}