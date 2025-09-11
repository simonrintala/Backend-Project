package com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;

/************************
 * IIdValidationService
 * ---
 * this interface allows other classes to use the IdValidationService class methods
 * without having an instance of the IdValidationService class, allowing looser coupling
 * between the different service classes
 ***********************/

public interface IIdValidationService {
    default User validateUserIdAndReturnUser(String id) {return null;}
    default Listing validateListingIdAndReturnListing(String id) {return null;}
    default Booking validateBookingIdAndReturnBooking(String id) {return null;}


}
