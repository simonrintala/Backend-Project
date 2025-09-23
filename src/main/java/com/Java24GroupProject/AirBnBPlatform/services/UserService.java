package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterResponse;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserResponse;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserUpdateRequest;
import com.Java24GroupProject.AirBnBPlatform.exceptions.NameAlreadyBoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.models.User;
// Removed BookingStatus enum usage; compare against String statuses
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ReviewRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserAuthRepository;
import com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation.IdValidationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final ListingRepository listingRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final UserAuthRepository userAuthRepository;
    private final IdValidationService idValidationService;


    //constructor injection
    public UserService(UserAuthRepository userAuthRepository, IdValidationService idValidationService, UserRepository userRepository, PasswordEncoder passwordEncoder, ListingRepository listingRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository) {
        this.passwordEncoder = passwordEncoder;
        this.listingRepository = listingRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.userAuthRepository = userAuthRepository;
        this.idValidationService = idValidationService;
    }

    //METHODS used by USER CONTROLLER CLASS -----------------------------------------------------------------------

    //register a new user, used by AuthenticationController
    public RegisterResponse registerUser(UserRequest userRequest) {
        //validate that username, email and phoneNr is unique
        //check if username already exists, and if is does, cast error
        if (userAuthRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new NameAlreadyBoundException("Username already registered to another user");
        }

        //same for email
        if (userAuthRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new NameAlreadyBoundException("Email already registered to another user");
        }

        //same for phoneNr
        if (userAuthRepository.findByPhoneNr(userRequest.getPhoneNr()).isPresent()) {
            throw new NameAlreadyBoundException("PhoneNr already registered to another user");
        }

        //maps the RegisterRequest to a new User entity
        User user = transferUserRequestToUser(userRequest, new User());
        //empty listing-favorites array list for a new user
        user.setFavorites(new ArrayList<>());

        //save new user
        userAuthRepository.save(user);

        return new RegisterResponse("user registered successfully", user.getUsername(), user.getRoles());
    }

    //get all users, return as UserResponseDTO
    public List<UserResponse> getAllUsers() {
        List<User> users = userAuthRepository.findAll();
        return users.stream()
                .map(this::transferUserToUserResponse)
                .collect(Collectors.toList());
    }

    //get current user
    public UserResponse getCurrentUser() {
        User currentUser = userAuthRepository.authenticateAndExtractUser();
        return transferUserToUserResponse(currentUser);
    }

    //get single user using id, return as UserResponse
    public UserResponse getUserById(String id) {
        User user = idValidationService.validateUserIdAndReturnUser(id);
        return transferUserToUserResponse(user);
    }

    //delete current user
    public void deleteCurrentUser() {
        User currentUser = userAuthRepository.authenticateAndExtractUser();
        deleteUser(currentUser);
    }

    //delete single user using id
    public void deleteUserById(String id) {
        User user = idValidationService.validateUserIdAndReturnUser(id);
        deleteUser(user);
    }

    //update current user data
    public UserResponse updateCurrentUser(UserRequest userRequest) {
        //get current user
        User currentUser = userAuthRepository.authenticateAndExtractUser();

        //if username is changed, check that username is not taken
        if (!currentUser.getUsername().equals(userRequest.getUsername())) {
            if (userAuthRepository.findByUsername(userRequest.getUsername()).isPresent()) {
                throw new NameAlreadyBoundException("Username already registered to another user");
            }
        }

        //same for email
        if (!currentUser.getEmail().equals(userRequest.getEmail())) {
            if (userAuthRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                throw new NameAlreadyBoundException("Email already registered to another user");
            }
        }

        //same for phoneNr
        if (!currentUser.getPhoneNr().equals(userRequest.getPhoneNr())) {
            if (userAuthRepository.findByPhoneNr(userRequest.getPhoneNr()).isPresent()) {
                throw new NameAlreadyBoundException("PhoneNr already registered to another user");
            }
        }

        //update current user
        currentUser = transferUserRequestToUser(userRequest, currentUser);

        //set updated at to current time
        currentUser.setUpdatedAt(LocalDateTime.now());
        userAuthRepository.save(currentUser);

        //convert to a responseDTO and return
        return transferUserToUserResponse(currentUser);
    }

    //add or remove a listing from current users saved favorites using listing id as an input variable
    public List<String> addOrRemoveFavorite(String listingId) {
        idValidationService.validateListingIdAndReturnListing(listingId);
        //get current user
        User user = userAuthRepository.authenticateAndExtractUser();

        boolean isRemoved = false;
        //loop through favorites to check if newListing is already saved
        for (String favoritesListingId : user.getFavorites()) {
            if (favoritesListingId.equals(listingId)) {
                user.removeFavorite(favoritesListingId);
                isRemoved = true;
                break;
            }
        }

        if (!isRemoved) {
            //check that does not already have max amount of saved favorites (max allowed = 20)
            if (user.getFavorites().size() >= 20) {
                throw new com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException("New favorite cannot be added, max 20 favorites allowed");
            }
            user.addFavorite(listingId);
        }

        userAuthRepository.save(user);

        return getFavorites();
    }

    //get favorites for current user
    public List<String> getFavorites() {
        //get current user
        User user = userAuthRepository.authenticateAndExtractUser();


        if (!user.getFavorites().isEmpty()) {
            for (String favoritesListingId : user.getFavorites()) {
                if (listingRepository.findById(favoritesListingId).isEmpty()) {
                    user.removeFavorite(favoritesListingId);

                }
            }
        }

        userAuthRepository.save(user);

        return user.getFavorites();
    }

    //METHODS used by this or other SERVICE CLASSES --------------------------------------------------------------

    //find a user via username, throw error if not found - used by AuthenticationController class for login-method
    public User findByUsername(String username) {
        return userAuthRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //used by class methods deleteUserById and deleteCurrentUser
    private void deleteUser(User user) {
        //get and delete user listings
        List<Listing> userListings= listingRepository.deleteByHost(user);

        //delete bookings and reviews for the deleted listings
        for (Listing listing : userListings) {
            bookingRepository.deleteByListing(listing);
            reviewRepository.deleteByListing(listing);
        }

        //get and delete bookings belonging to the user
        List<Booking> userBookings = bookingRepository.deleteByUser(user);

        //loop bookings and add back dates to listing if booking is pending
        for (Booking booking : userBookings) {
            if ("PENDING".equals(booking.getBookingStatus())) {

                Listing listing = idValidationService.validateListingIdAndReturnListing(booking.getListing().getId());
                listing.addAvailableDateRange(booking.getBookingDates());
                listing.setUpdatedAt(LocalDateTime.now());
                listingRepository.save(listing);
            }
        }

        //delete user from reviews (reviews are not deleted, but user is set to null)
        List<Review> userReviews = reviewRepository.findByUser(user);
        if (!userReviews.isEmpty()) {
            for (Review userReview : userReviews) {
                userReview.setUser(null);
                userReview.setUsername("[deleted user]");
            }
        }
        userAuthRepository.delete(user);
    }

    //convert incoming DTO (from UserController) to User object
    private User transferUserRequestToUser(UserRequest userRequest, User user) {

        user.setUsername(userRequest.getUsername());
        //encodes the password
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNr(userRequest.getPhoneNr());
        //create UserAddress from String variables from the UserRequest
        user.setAddress(new UserAddress(userRequest.getStreet(), userRequest.getZipCode(), userRequest.getCity(), userRequest.getCountry()));
        user.setProfilePictureURL(userRequest.getProfilePictureURL());
        user.setDescription(userRequest.getDescription());

        //assign the role USER if no roles are specified in UserRequest
        if(userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        } else {
            user.setRoles(userRequest.getRoles());
        }

        return user;
    }

    //transfer User to UserResponse, used when returning user data to UserController
    private UserResponse transferUserToUserResponse(User user) {
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNr(),
                user.getAddress(),
                user.getProfilePictureURL(),
                user.getDescription(),
                user.getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }


    // PATCH
    public User updateUserInfo(UserUpdateRequest updatedInfo) {
        User currentUser = userAuthRepository.authenticateAndExtractUser();

        if (updatedInfo.getPhoneNr() != null) {
            currentUser.setPhoneNr(updatedInfo.getPhoneNr());
        }

        if (updatedInfo.getEmail() != null) {
            currentUser.setEmail(updatedInfo.getEmail());
        }
        if (updatedInfo.getAddress() != null) {
            currentUser.setAddress(updatedInfo.getAddress());
        }

        return userAuthRepository.save(currentUser);
    }

}
