package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.services.ListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listings")
public class ListingController {
    private final ListingService listingService;
    
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ListingResponse> createListing(@Valid @RequestBody Listing listing) {
        ListingResponse newListing = listingService.createListing(listing);
        return new ResponseEntity<>(newListing, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ListingResponse>> getAllListings() {
        List<ListingResponse> listings = listingService.getAllListings();
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getListingById(@PathVariable String id) {
        ListingResponse listing = listingService.getListingById(id);
        return new ResponseEntity<>(listing, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ListingResponse> updateListing(@PathVariable String id, @Valid @RequestBody Listing listing) {
        return ResponseEntity.ok(listingService.updateListing(id, listing));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<Void> deleteListing(@PathVariable String id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
