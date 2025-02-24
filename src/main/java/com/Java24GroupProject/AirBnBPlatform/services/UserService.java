package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterResponse;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.NameAlreadyBoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//NOTE: not finished, just made what needed to be there for Security implementation.

//This class has methods relating to Spring security, but should also be expanded to contain methods relevant to user CRUD etc.
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

        //maps the RegisterRequest to a User entity
        User user = new User();
        user.setUsername(userRequest.getUsername());
        //encodes the password
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNr(userRequest.getPhoneNr());
        user.setAddress(new UserAddress(userRequest.getStreet(), userRequest.getZipCode(), userRequest.getCity(), userRequest.getCountry()));
        user.setProfilePictureURL(userRequest.getProfilePictureURL());
        user.setDescription(userRequest.getDescription());
        //empty listing-favorites array list for a new user
        user.setFavorites(new ArrayList<>());

        //if no roles are provided in RegisterRequest, set to user
        if(userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        } else {
            user.setRoles(userRequest.getRoles());
        }

        user.setUpdatedAt(null);
        userRepository.save(user);

        return new RegisterResponse(
                "user registered successfully",
                user.getUsername(),
                user.getRoles());
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userResponseList.add(convertUsertoUserResponse(user));
        }
        return userResponseList;
    }

    public UserResponse getUserById(String id) {
        User user = validateUserIdAndReturnUser(id);
        return convertUsertoUserResponse(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(validateUserIdAndReturnUser(id));
    }

    public UserResponse updateUser(UserRequest userRequest) {
        //validate that username, email and phoneNr in updated user info are unique
        validateUniqueFields(userRequest);



        //validate user id and get existing user
        User existingUser = verifyCookiesAndExtractUser();

        existingUser.setUsername(userRequest.getUsername());
        //encodes the password
        existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhoneNr(userRequest.getPhoneNr());
        existingUser.setAddress(new UserAddress(userRequest.getStreet(), userRequest.getZipCode(), userRequest.getCity(), userRequest.getCountry()));
        existingUser.setProfilePictureURL(userRequest.getProfilePictureURL());
        existingUser.setDescription(userRequest.getDescription());
        existingUser.setUpdatedAt(LocalDateTime.now());

        if(userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            existingUser.setRoles(Set.of(Role.USER));
        } else {
            existingUser.setRoles(userRequest.getRoles());
        }

        return convertUsertoUserResponse(existingUser);
    }

    public String addFavorite(String listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing id does not exist in database"));

        User user = verifyCookiesAndExtractUser();

        if (user.getFavorites().contains(listing)) {
            throw new IllegalArgumentException("listing already in favorites, could not be added");
        }
        user.getFavorites().add(listing);
        return listing.getTitle();
    }

    public String removeFavorite(String listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing id does not exist in database"));

        User user = verifyCookiesAndExtractUser();

        if (!user.getFavorites().contains(listing)) {
            throw new IllegalArgumentException("listing not in favorites, could not be removed");
        }
        user.getFavorites().remove(listing);
        return listing.getTitle();
    }

    //find a user via username, throw error if not found - used by AuthenticationController class for login-method
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //check if user id exists in database and if so return user. Converts Optional<User> (returned by Repository), to User
    private User validateUserIdAndReturnUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User id does not exist in database"));
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


    private UserResponse convertUsertoUserResponse(User user) {
        List<String> favorites = new ArrayList<>();
        for (Listing listing : user.getFavorites()) {
            favorites.add(listing.getTitle());
        }
        return new UserResponse(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNr(), user.getAddress(), user.getProfilePictureURL(), user.getDescription(), favorites, user.getRoles(), user.getCreatedAt(), user.getUpdatedAt());
    }

    private User verifyCookiesAndExtractUser() {
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
