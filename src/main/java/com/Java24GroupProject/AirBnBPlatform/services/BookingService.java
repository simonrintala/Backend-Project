package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectDeleteBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.DeleteBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateUpdateBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.UpdateBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.GetBookingMethodService;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.BookingStateProcessor;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.IStateHandler;
import org.springframework.stereotype.Service;

import java.util.List;

/************************
 * BookingService
 * ---
 * This class contains objects holding the logic for the create, update, accept/reject and delete booking methods,
 * and has a dependency injection for the GetBookingMethodsService (which contains all booking GET-methods).
 * ---
 * It is the interaction point between the BookingController and the different classes coordinating the logic
 * of different booking-related operations.
 * **********************/

@Service
public class BookingService {
    private final CreateUpdateBookingTemplate bookingCreateMethod;
    private final CreateUpdateBookingTemplate bookingUpdateMethod;
    private final GetBookingMethodService getBookingMethodService;
    private final AcceptRejectDeleteBookingTemplate deleteBookingMethod;
    private final AcceptRejectDeleteBookingTemplate acceptRejectBookingMethod;

    public BookingService(BookingStateProcessor bookingStateProcessor, BookingDTOConversionService bookingDTOConversionService, UserAuthRepository userAuthRepository, IdValidationService idValidationService, BookingRepository bookingRepository, ListingRepository listingRepository, GetBookingMethodService getBookingMethodService, DateAvailabilityService dateAvailabilityService) {
        this.getBookingMethodService = getBookingMethodService;
        bookingCreateMethod = new CreateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, dateAvailabilityService, bookingStateProcessor, listingRepository);
        bookingUpdateMethod = new UpdateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, dateAvailabilityService);
        acceptRejectBookingMethod = new AcceptRejectBookingMethod(userAuthRepository, idValidationService, listingRepository, bookingRepository, bookingStateProcessor, bookingDTOConversionService);
        deleteBookingMethod = new DeleteBookingMethod(userAuthRepository,idValidationService, listingRepository, bookingRepository, bookingStateProcessor);
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

    public BookingResponse acceptOrRejectBooking(String id, IStateHandler iStateHandler) {
        return acceptRejectBookingMethod.acceptRejectDelete(id, iStateHandler);
    }

    public void deleteBooking(String id) {
        deleteBookingMethod.acceptRejectDelete(id, null);
    }

}

