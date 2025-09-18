package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/************************
 * AuthenticationService
 * ---
 * This class contain methods for authenticating and extracting the current user from jwtTokens/cookies,
 * and for validating the current users roles and database id.
 * -
 * The authenticateAndExtractUser() method was previously a static method in the UserService class
 * (used by UserService, ListingService, BookingService and ReviewService) was separated into this interface instead
 * based on the Single Responsibility Principle.
 * -
 * Methods for validating if the current user corresponds to a specific userId and/or has a certain ROLE has been added.
 * (These checks were previously not separate methods, but part on methods in the different Service classes.)
 ***********************/

public interface UserAuthRepository extends UserRepository {

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

    /*checks if the current user corresponds to a userId
    (is used e.g., for verifying if current user owns a booking they are trying to modify)*/
    default boolean isSameAsCurrentUser(User user) {
        User currentUser = authenticateAndExtractUser();
        return currentUser.getId().equals(user.getId());
    }

    //check if the current user has a specific role (used to validate user privilege to access e.g. admin methods)
    default boolean doesCurrentUserHaveThisRole(Role role) {
        User currentUser = authenticateAndExtractUser();
        return currentUser.getRoles().contains(role);
    }

    default boolean isSameAsCurrentUserOrHasRole(User user, Role role) {
        return (isSameAsCurrentUser(user) || doesCurrentUserHaveThisRole(role));
    }
}
