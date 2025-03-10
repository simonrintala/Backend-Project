package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserResponse;
import com.Java24GroupProject.AirBnBPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //USER endpoints  ------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getCurrentUser() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.updateCurrentUser(userRequest), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
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

    //ADMIN endpoints ---------------------------------------------------------------------

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
