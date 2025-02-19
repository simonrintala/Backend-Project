package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.services.ListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listings")
public class ListingController {
    private final ListingService listingService;
    
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
    
    //REMEMBER LISTINGS PERMIT IS SET TO ALL
    
    @PostMapping
    public ResponseEntity<Listing> createListing(@RequestBody Listing listing) {
        Listing newListing = listingService.createListing(listing);
        return new ResponseEntity<>(newListing, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings() {
        List<Listing> listing = listingService.getAllListings();
        return new ResponseEntity<>(listing, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable String id) {
        Optional<Listing> listing = listingService.getListingById(id);
        return listing.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
