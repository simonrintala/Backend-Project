package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectBooking;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectDeleteTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.DeleteBooking;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateUpdateBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.UpdateBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.GetBookingMethodService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final CreateUpdateBookingTemplate bookingCreateMethod;
    private final CreateUpdateBookingTemplate bookingUpdateMethod;
    private final GetBookingMethodService getBookingMethodService;
    private final AcceptRejectDeleteTemplate deleteBookingMethod;
    private final AcceptRejectDeleteTemplate acceptRejectBookingMethod;

    public BookingService(BookingDTOConversionService bookingDTOConversionService, UserAuthRepository userAuthRepository, IdValidationService idValidationService, BookingRepository bookingRepository, ListingRepository listingRepository, GetBookingMethodService getBookingMethodService, DateAvailabilityService dateAvailabilityService) {
        this.getBookingMethodService = getBookingMethodService;
        bookingCreateMethod = new CreateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, dateAvailabilityService);
        bookingUpdateMethod = new UpdateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, dateAvailabilityService);
        acceptRejectBookingMethod = new AcceptRejectBooking(userAuthRepository, idValidationService, listingRepository, bookingRepository, bookingDTOConversionService);
        deleteBookingMethod = new DeleteBooking(userAuthRepository,idValidationService, listingRepository, bookingRepository);
    }

    //METHODS used by BOOKING CONTROLLER CLASS -----------------------------------------------------------------------
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        return bookingCreateMethod.createUpdateBooking(bookingRequest);
    }

    public BookingResponse updateBooking(String bookingId, BookingRequest updatedBookingRequest) {
        return bookingUpdateMethod.createUpdateBooking(updatedBookingRequest, bookingId);
    }

    //get bookings by id
    public BookingResponse getBookingById(String id) {
        return getBookingMethodService.getBookingById(id);
    }

    //get all bookings
    public List<BookingResponse> getAllBookings() {
        return getBookingMethodService.getAllBookings();
    }

    //get bookings any user
    public List<BookingResponse> getBookingsByUserId(String userId) {
        return getBookingMethodService.getBookingsByUserId(userId);
    }

    //get bookings current user
    public List<BookingResponse> getBookingsCurrentUser() {
        return getBookingMethodService.getBookingsCurrentUser();
    }

    //get all bookings for current user's listings
    public List<BookingResponse> getBookingsForListingsOfCurrentUser() {
        return getBookingMethodService.getBookingsForListingsOfCurrentUser();
    }

    //get all bookings for a listing using listingId
    public List<BookingResponse> getBookingsByListingId(String listingId) {
        return getBookingMethodService.getBookingsByListingId(listingId);
    }

    public BookingResponse acceptOrRejectBooking(String id, BookingStatus bookingStatus) {
        return acceptRejectBookingMethod.acceptRejectDelete(id, bookingStatus);
    }

    public void deleteBooking(String id) {
        deleteBookingMethod.acceptRejectDelete(id);
    }

}

