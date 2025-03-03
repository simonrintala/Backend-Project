package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserResponse;
import com.Java24GroupProject.AirBnBPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*registration of new users is handled by AuthenticationController*/
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id) {
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(id, userRequest), HttpStatus.OK);
    }

    //adds a listing to current user's favorites if not already saved, otherwise removes the listing from favorites
    @PatchMapping("/favorites/{listingId}")
    public ResponseEntity<String> addOrRemoveFavorite(@PathVariable String listingId) {
        return new ResponseEntity<>(userService.addOrRemoveFavorite(listingId), HttpStatus.OK);
    }

    //get favorites for current users
    @GetMapping("/favorites")
    public ResponseEntity<Map<String,String>> getFavorites() {
        return new ResponseEntity<>(userService.getFavorites(), HttpStatus.OK);
    }
}
