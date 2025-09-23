package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import com.Java24GroupProject.AirBnBPlatform.services.BookingDTOConversionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingDecisionService {
    /**
     * Use case: host decides to accept or reject a booking.
     *
     * This class coordinates:
     * - Authz checks (only the listing host can decide)
     * - Ensuring the booking is still pending
     * - Applying the state (Accept/Reject) via BookingStateProcessor
     * - Saving and returning a response DTO
     */
    private final UserAuthRepository authenticationService;
    private final IdValidationService idValidationService;
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    private final BookingDTOConversionService bookingDTOConversionService;
    private final BookingStateProcessor bookingStateProcessor;

    public BookingDecisionService(UserAuthRepository authenticationService,
                                  IdValidationService idValidationService,
                                  BookingRepository bookingRepository,
                                  ListingRepository listingRepository,
                                  BookingDTOConversionService bookingDTOConversionService,
                                  BookingStateProcessor bookingStateProcessor) {
        this.authenticationService = authenticationService;
        this.idValidationService = idValidationService;
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
        this.bookingDTOConversionService = bookingDTOConversionService;
        this.bookingStateProcessor = bookingStateProcessor;
    }

    public BookingResponse acceptOrRejectBooking(String id, boolean isAccepted) {
        Booking booking = idValidationService.validateBookingIdAndReturnBooking(id);

        if (!"PENDING".equals(booking.getBookingStatus())) {
            throw new UnauthorizedException("Booking has already been accepted or rejected");
        }

        User currentUser = authenticationService.authenticateAndExtractUser();
        Listing listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());

        if (!authenticationService.isSameAsCurrentUser(listing.getHost())) {
            throw new UnauthorizedException("only the listing host can accept/reject a booking");
        }

        if (isAccepted) {
            bookingStateProcessor.accept(booking, listing, listingRepository);
        } else {
            bookingStateProcessor.reject(booking, listing, listingRepository);
        }

        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return bookingDTOConversionService.convertToDTOResponse(booking);
    }
}


