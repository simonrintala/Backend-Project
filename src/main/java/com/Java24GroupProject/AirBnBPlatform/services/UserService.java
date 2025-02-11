package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void registerUser(User user) {

        //hash the password and store an encoded version in database
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //if no other role is specified, set role as USER
        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }
        userRepository.save(user);
    }

    //find a user via username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //check if username already exists in database
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    //check if email already exists in database
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    //check if phone number already exists in database
    public boolean existsByPhoneNr(String phoneNr) {
        return userRepository.findByPhoneNr(phoneNr).isPresent();
    }

}
