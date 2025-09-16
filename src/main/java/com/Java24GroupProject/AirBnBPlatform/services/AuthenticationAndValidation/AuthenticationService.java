package com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation;

import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/************************
 * AuthenticationService
 * ---
 * This class contain the method for authenticating and extracting the current user from jwtTokens/cookies
 * The authenticateAndExtractUser() method was previously a static method in the UserService class (used by UserService,
 * ListingService, BookingService and ReviewService) was separated into this interface instead based on the Single
 * Responsibility Principle.
 *
 * Methods for validating if the current user corresponds to a specific userId and/or has a certain ROLE has been added.
 * (These checks were previously not separate methods, but part on methods in the different Service classes.)
 ***********************/

public interface AuthenticationService extends UserRepository {

    //authenticate and extract current logged-in user, cast error if no user is logged-in or cannot be found in database
    default User authenticateAndExtractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not logged in.");
        }

        //get user id from token via userDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }
}
