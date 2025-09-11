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
 * this class contain the methods for authenticating and extracting the current user from jwtTokens/cookies
 * this method was previously a static method in the UserService class and
 * were separated into this interface instead based on the Single Responsibility Principle
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
