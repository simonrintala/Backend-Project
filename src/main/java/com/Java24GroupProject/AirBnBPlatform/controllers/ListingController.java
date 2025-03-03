package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingRequest;
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
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    public ResponseEntity<ListingResponse> createListing(@Valid @RequestBody ListingRequest listingRequest) {
        ListingResponse listingResponse = listingService.createListing(listingRequest);
        return new ResponseEntity<>(listingResponse, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ListingResponse>> getAllListings() {
        List<ListingResponse> listings = listingService.getAllListings();
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    // search for listing by id
    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getListingById(@PathVariable String id) {
        ListingResponse listingResponse = listingService.getListingById(id);
        return new ResponseEntity<>(listingResponse, HttpStatus.OK);
    }
    
    @GetMapping("/hostId/{hostId}")
    public ResponseEntity<List<ListingResponse>> getListingsByHostId(@PathVariable String hostId) {
        List<ListingResponse> listings = listingService.getAllListingsByHostId(hostId);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    // search for listing between price range
    @GetMapping("/price")
    public ResponseEntity<List<ListingResponse>> getAllListingsByPrice(@RequestParam double minPrice, @RequestParam double maxPrice) {
        List<ListingResponse> listings = listingService.getListingByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    //search for listing via location (city)
    @GetMapping("/location/{location}")
    public ResponseEntity<List<ListingResponse>> getAllListingsByLocation(@PathVariable String location) {
        List<ListingResponse> listings = listingService.getListingByLocation(location);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    // search for listing between capacity size
    @GetMapping("/capacity")
    public ResponseEntity<List<ListingResponse>> getAllListingsByCapacity(@RequestParam double minCapacity, @RequestParam double maxCapacity) {
        List<ListingResponse> listing = listingService.getListingByCapacity(minCapacity, maxCapacity);
        return new ResponseEntity<>(listing, HttpStatus.OK);
    }
    
    // search for listing with matching utilities
    @GetMapping("/utilities/{utilities}")
    public ResponseEntity<List<ListingResponse>> getAllListingsByUtilities(@PathVariable String utilities) {
        List<ListingResponse> listings = listingService.getListingByUtilities(utilities);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOST','ADMIN')")
    public ResponseEntity<ListingResponse> updateListing(@PathVariable String id, @Valid @RequestBody Listing listing) {
        return ResponseEntity.ok(listingService.updateListing(id, listing));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOST','ADMIN')")
    public ResponseEntity<String> deleteListing(@PathVariable String id) {
        String deleteMessage = listingService.deleteListing(id);
        return ResponseEntity.ok(deleteMessage);
    }
}
