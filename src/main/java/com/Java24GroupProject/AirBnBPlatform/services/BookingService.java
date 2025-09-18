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
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.GetBookingsMethods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final CreateUpdateBookingTemplate bookingCreateMethod;
    private final CreateUpdateBookingTemplate bookingUpdateMethod;
    private final GetBookingsMethods getBookingsMethods;
    private final AcceptRejectDeleteTemplate deleteBooking;
    private final AcceptRejectDeleteTemplate acceptRejectBooking;

    public BookingService(BookingDTOConversionService bookingDTOConversionService, UserAuthRepository userAuthRepository, IdValidationService idValidationService, BookingRepository bookingRepository, ListingRepository listingRepository) {
        bookingCreateMethod = new CreateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, new DateAvailabilityService(listingRepository));
        bookingUpdateMethod = new UpdateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, new DateAvailabilityService(listingRepository));
        getBookingsMethods = new GetBookingsMethods(listingRepository, idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository);
        acceptRejectBooking = new AcceptRejectBooking(userAuthRepository, idValidationService, listingRepository, bookingRepository, bookingDTOConversionService);
        deleteBooking = new DeleteBooking(userAuthRepository,idValidationService, listingRepository, bookingRepository);
    }

    //METHODS used by BOOKING CONTROLLER CLASS -----------------------------------------------------------------------


    public BookingResponse createBooking(BookingRequest bookingRequest) {
        return bookingCreateMethod.createUpdateBooking(bookingRequest, null);
    }

    public BookingResponse updateBooking(String bookingId, BookingRequest updatedBookingRequest) {
        return bookingUpdateMethod.createUpdateBooking(updatedBookingRequest, bookingId);
    }

    //get bookings by id
    public BookingResponse getBookingById(String id) {
        return getBookingsMethods.getBookingById(id);
    }

    //get all bookings
    public List<BookingResponse> getAllBookings() {
        return getBookingsMethods.getAllBookings();
    }

    //get bookings any user
    public List<BookingResponse> getBookingsByUserId(String userId) {
        return getBookingsMethods.getBookingsByUserId(userId);
    }

    //get bookings current user
    public List<BookingResponse> getBookingsCurrentUser() {
        return getBookingsMethods.getBookingsCurrentUser();
    }

    //get all bookings for current user's listings
    public List<BookingResponse> getBookingsForListingsOfCurrentUser() {
        return getBookingsMethods.getBookingsForListingsOfCurrentUser();
    }

    //get all bookings for a listing using listingId
    public List<BookingResponse> getBookingsByListingId(String listingId) {
        return getBookingsMethods.getBookingsByListingId(listingId);
    }

    public BookingResponse acceptOrRejectBooking(String id, BookingStatus bookingStatus) {
        return acceptRejectBooking.acceptRejectDelete(id, bookingStatus);
    }

    public void deleteBooking(String id) {
        deleteBooking.acceptRejectDelete(id, null);
    }

}

