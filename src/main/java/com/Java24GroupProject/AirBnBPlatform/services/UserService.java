package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterResponse;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.NameAlreadyBoundException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ListingRepository listingRepository;

    //constructor injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.listingRepository = listingRepository;
    }

    //method for registering a new user
    public RegisterResponse registerUser(UserRequest userRequest) {
        //validate that username, email and phoneNr is unique
        validateUniqueFields(userRequest);

        //maps the RegisterRequest to a new User entity
        User user = transferUserRequestToUser(userRequest, new User());

        //empty listing-favorites array list for a new user
        user.setFavorites(new ArrayList<>());
        user.setUpdatedAt(null);

        //save new user
        userRepository.save(user);

        return new RegisterResponse("user registered successfully", user.getUsername(), user.getRoles());
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userResponseList.add(transferUserToUserResponse(user));
        }
        return userResponseList;
    }

    public UserResponse getUserById(String id) {
        User user = validateUserIdAndReturnUser(id);
        return transferUserToUserResponse(user);
    }

    public void deleteUserById(String id) {
        userRepository.delete(validateUserIdAndReturnUser(id));
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {
        //validate user id and get and update existing user
        User existingUser = transferUserRequestToUser(userRequest, validateUserIdAndReturnUser(id));

        //set updated at to current time
        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        //convert to a responseDTO and return
        return transferUserToUserResponse(existingUser);
    }

    //add or remove a listing from current users saved favorites using listing id as an input variable
    public String addOrRemoveFavorite(String listingId) {
        Listing newListing = listingRepository.findById(listingId)
                .orElseThrow(() -> new java.lang.IllegalArgumentException("Listing id does not exist in database"));

        String message = "'"+ newListing.getTitle()+"'";
        //get current user
        User user = verifyAuthenticationAndExtractUser(userRepository);

        boolean isRemoved = false;
        //loop through favorites to check if newListing is already saved
        for (Listing listingReference : user.getFavorites()) {
            if (listingRepository.findById(listingReference.getId()).isPresent()) {
                Listing listingInFavorites = listingRepository.findById(listingReference.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
                //if listing is already in favorites, remove from favorites
                if (listingInFavorites.getId().equals(newListing.getId())) {
                    user.removeFavorite(listingReference);
                    message = message + " has been removed from favorites";
                    isRemoved = true;
                    break;
                }
                //if listing has been deleted, remove from favorites
            } else {
                user.removeFavorite(listingReference);

            }
        }

        if (!isRemoved) {
            //check that does not already have max amount of saved favorites (max allowed = 20)
            if (user.getFavorites().size() >= 20) {
                throw new com.Java24GroupProject.AirBnBPlatform.exceptions.UnsupportedOperationException("New favorite cannot be added, max 20 favorites allowed");
            }
            user.addFavorite(newListing);
            message = message +" has been added to favorites";
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return message;
    }

    //get favorites for current user
    public Map<String, String> getFavorites() {
        //get current user
        User user = verifyAuthenticationAndExtractUser(userRepository);

        //convert list of listings to list of string objects
        Map<String, String> favoritesResponse = new HashMap<>();
        if (!user.getFavorites().isEmpty()) {
            for (Listing listingReference : user.getFavorites()) {
                if (listingRepository.findById(listingReference.getId()).isPresent()) {
                    Listing listing = listingRepository.findById(listingReference.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
                    favoritesResponse.put(listing.getId(), listing.getTitle());
                } else {
                    //if listing has been removed from database, delete if from favorites
                    user.removeFavorite(listingReference);
                    userRepository.save(user);
                }
            }
        }
        return favoritesResponse;
    }

    //find a user via username, throw error if not found - used by AuthenticationController class for login-method
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //check if user id exists in database and if so return user. Converts Optional<User> (returned by Repository), to User
    private User validateUserIdAndReturnUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new java.lang.IllegalArgumentException("User id does not exist in database"));
    }

    private void validateUniqueFields(UserRequest userRequest) {
        //check if username already exists, and if is does, cast error
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new NameAlreadyBoundException("Username already exists in database");
        }

        //same for email
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new NameAlreadyBoundException("Email already exists in database");
        }

        //same for phoneNr
        if (userRepository.findByPhoneNr(userRequest.getPhoneNr()).isPresent()) {
            throw new NameAlreadyBoundException("PhoneNr already exists in database");
        }
    }

    //convert incoming DTO (from UserController) to User
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

        //assign the role USER if not roles are specified
        if(userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        } else {
            user.setRoles(userRequest.getRoles());
        }

        return user;
    }

    public UserResponse transferUserToUserResponse(User user) {
        return new UserResponse(user.getUsername(), user.getEmail(), user.getPhoneNr(), user.getAddress(), user.getProfilePictureURL(), user.getDescription(), getFavorites(), user.getRoles(), user.getCreatedAt(), user.getUpdatedAt());
    }

    //verify and get current user from jwtToken/cookies
    public static User verifyAuthenticationAndExtractUser(UserRepository userRepository) {
        //check that user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("user is not authenticated");
        }
        //get user id from token via userDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
