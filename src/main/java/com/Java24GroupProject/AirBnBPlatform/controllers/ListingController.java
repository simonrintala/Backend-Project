package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.services.ListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listings")
public class ListingController {
    private final ListingService listingService;
    
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
    
    @PostMapping
    public ResponseEntity<Listing> createListing(@Valid @RequestBody Listing listing) {
        Listing newListing = listingService.createListing(listing);
        return new ResponseEntity<>(newListing, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ListingResponse>> getAllListings() {
        List<ListingResponse> listing = listingService.getAllListings();
        return new ResponseEntity<>(listing, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable String id) {
        Optional<Listing> listing = listingService.getListingById(id);
        return listing.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // search for listing between price range
    @GetMapping("/price")
    public ResponseEntity<List<Listing>> getAllListingsByPrice(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        List<Listing> listings = listingService.getListingByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Listing>> getAllListingsByLocation(@PathVariable String location) {
        List<Listing> listings = listingService.getListingByLocation(location);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable String id, @Valid @RequestBody Listing listing) {
        return ResponseEntity.ok(listingService.updateListing(id, listing));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable String id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
