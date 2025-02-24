package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.NameAlreadyBoundException;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//NOTE: not finished, just made what needed to be there for Security implementation.

//This class has methods relating to Spring security, but should also be expanded to contain methods relevant to user CRUD etc.
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //constructor injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //method for registering a new user
    public RegisterResponse registerUser(RegisterRequest registerRequest) {

        //check if username already exists, and if is does, cast error
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new NameAlreadyBoundException("Username already exists in database");
        }

        //same for email
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new NameAlreadyBoundException("Email already exists in database");
        }

        //same for phoneNr
        if (userRepository.findByPhoneNr(registerRequest.getPhoneNr()).isPresent()) {
            throw new NameAlreadyBoundException("PhoneNr already exists in database");
        }

        //maps the RegisterRequest to a User entity
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        //encodes the password
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNr(registerRequest.getPhoneNr());
        user.setAddress(new UserAddress(registerRequest.getStreet(), registerRequest.getZipCode(), registerRequest.getCity(), registerRequest.getCountry()));
        user.setProfilePictureURL(registerRequest.getProfilePictureURL());
        user.setDescription(registerRequest.getDescription());
        //empty listing-favorites array list for a new user
        user.setFavorites(new ArrayList<>());

        //if no roles are provided in RegisterRequest, set to user
        if(registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        } else {
            user.setRoles(registerRequest.getRoles());
        }

        user.setUpdatedAt(null);
        userRepository.save(user);

        return new RegisterResponse(
                "user registered successfully",
                user.getUsername(),
                user.getRoles());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return validateUserIdAndReturnUser(id);
    }

    public void deleteUser(String id) {
        userRepository.delete(validateUserIdAndReturnUser(id));
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

}
