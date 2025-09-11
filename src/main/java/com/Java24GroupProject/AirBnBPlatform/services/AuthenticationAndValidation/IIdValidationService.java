package com.Java24GroupProject.AirBnBPlatform.services.IdValidation;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;

public interface IIdValidationService {
    User validateUserIdAndGetUser(String id);
    Listing validateListingIdAndGetListing(String id);
    Booking validateBookingIdAndGetBooking(String id);


}
