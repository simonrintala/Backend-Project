package com.Java24GroupProject.AirBnBPlatform.services.BookingMethods;

import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.AcceptRejectDeleteBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.AcceptRejectDeleteBooking.DeleteBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.CreateUpdateBookingTemplate;
import com.Java24GroupProject.AirBnBPlatform.services.BookingMethods.CreateUpdateBooking.UpdateBookingMethod;
import com.Java24GroupProject.AirBnBPlatform.services.DateAvailabilityService;
import com.Java24GroupProject.AirBnBPlatform.services.StatesBooking.BookingStateProcessor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookingMethodService {
    private final CreateUpdateBookingTemplate bookingCreateMethod;
    private final CreateUpdateBookingTemplate bookingUpdateMethod;
    private final AcceptRejectDeleteBookingTemplate deleteBookingMethod;
    private final AcceptRejectDeleteBookingTemplate acceptRejectBookingMethod;
    private final Map<BookingMethodEnum, BookingMethod> bookingMethodMap;

    public enum BookingMethodEnum {CREATE, UPDATE, ACC_REJ, DELETE}

    public BookingMethodService(BookingStateProcessor bookingStateProcessor, BookingDTOConversionService bookingDTOConversionService, UserAuthRepository userAuthRepository, IdValidationService idValidationService, BookingRepository bookingRepository, ListingRepository listingRepository, DateAvailabilityService dateAvailabilityService) {
        bookingCreateMethod = new CreateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, dateAvailabilityService, bookingStateProcessor, listingRepository);
        bookingUpdateMethod = new UpdateBookingMethod(idValidationService, userAuthRepository, bookingDTOConversionService,bookingRepository, dateAvailabilityService);
        acceptRejectBookingMethod = new AcceptRejectBookingMethod(userAuthRepository, idValidationService, listingRepository, bookingRepository, bookingStateProcessor, bookingDTOConversionService);
        deleteBookingMethod = new DeleteBookingMethod(userAuthRepository,idValidationService, listingRepository, bookingRepository, bookingStateProcessor);
        bookingMethodMap = new HashMap<> () {{
            put(BookingMethodEnum.CREATE, bookingCreateMethod);
            put(BookingMethodEnum.UPDATE, bookingUpdateMethod);
            put(BookingMethodEnum.ACC_REJ, acceptRejectBookingMethod);
            put(BookingMethodEnum.DELETE, deleteBookingMethod);
        }};
    }

    public BookingMethod setBookingMethod(BookingMethodEnum bookingMethodEnum) {
        return bookingMethodMap.get(bookingMethodEnum);
    }

}
