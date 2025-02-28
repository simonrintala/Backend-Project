package com.Java24GroupProject.AirBnBPlatform.services;


import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    
    public ListingService(ListingRepository listingRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }
    
    public Listing createListing(Listing listing) {
        if (listing.getTitle() == null || listing.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (listing.getPricePerNight() == null || listing.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than 0");
        }
        User user = userRepository.findById(listing.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        listing.setHost(user);
        return listingRepository.save(listing);
        
    }
    
    public List<ListingResponse> getAllListings() {
        List<Listing> listings = listingRepository.findAll();
        
        // convert Listing to ListingResponse
        return listings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<ListingResponse> getListingById(String id) {
        return listingRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<ListingResponse> getAllListingsByHostId(String hostId) {
        if (hostId == null || hostId.isEmpty()) {
            throw new IllegalArgumentException("HostId cannot be empty");
        }
        List<Listing> listings = listingRepository.findByHostId(hostId);
        if (listings.isEmpty()) {
            throw new ResourceNotFoundException("Listing not found");
        }
        return listings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ListingResponse> getListingByPriceRange(double minPrice, double maxPrice) {
        // make sure none of the prices are negative
        if (minPrice < 0 || maxPrice <= 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        // make sure minPrice is not greater that maxPrice
        if (minPrice > maxPrice ) {
            throw new IllegalArgumentException("Price cannot be greater than maxPrice");
        }
        
        List<Listing> listings = listingRepository.findByPricePerNightBetween(minPrice, maxPrice);
        if(listings.isEmpty()) {
            throw new ResourceNotFoundException("Listing not found");
        }
        return listings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ListingResponse> getListingByLocation(String location) {
        // make sure location isn't empty/null
        if(location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty or null");
        }
        
        List<Listing> listings = listingRepository.findByLocation(location);
        if(listings.isEmpty()) {
            throw new ResourceNotFoundException("No listing found for location: " + location);
        }
        return listings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ListingResponse> getListingByCapacity(double minCapacity, double maxCapacity) {
        if (minCapacity < 0 || maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        if (minCapacity > maxCapacity) {
            throw new IllegalArgumentException("minCapacity cannot be greater than maxCapacity");
        }
       
        List<Listing> listings = listingRepository.findByCapacityBetween(minCapacity, maxCapacity);
        if(listings.isEmpty()) {
            throw new ResourceNotFoundException("Listing not found");
        }
        return listings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ListingResponse> getListingByUtilities(String utility) {
        if(utility == null || utility.isEmpty()) {
            throw new IllegalArgumentException("Utility cannot be empty or null");
        }
        List<Listing> listings = listingRepository.findByUtilities(utility);
        if(listings.isEmpty()) {
            throw new ResourceNotFoundException("No listing found for utility: " + utility);
        }
        return listings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // PATCH
    public Listing updateListing(String id, Listing listing) {
        Listing existingListing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
       
        // only non null field will be updated
        if (listing.getTitle() != null) {
            existingListing.setTitle(listing.getTitle());
        }
        if (listing.getPricePerNight() != null) {
            existingListing.setPricePerNight(listing.getPricePerNight());
        }
        if(listing.getDescription() != null) {
            existingListing.setDescription(listing.getDescription());
        }
        if(listing.getCapacity() != null) {
            existingListing.setCapacity(listing.getCapacity());
        }
        if(!listing.getUtilities().isEmpty()) {
            existingListing.setUtilities(listing.getUtilities());
        }
        if(listing.getImage_urls() != null) {
            existingListing.setImage_urls(listing.getImage_urls());
        }
        if(listing.getLocation() != null) {
            existingListing.setLocation(listing.getLocation());
        }
        return listingRepository.save(existingListing);
    }
    
    public void deleteListing(String id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
        
        listingRepository.delete(listing);
    }
    
    
    
    
/*    // used the one from product_api project
    private void validateListing(Listing listing) {
        if(listing.getTitle() == null || listing.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if(listing.getPrice_per_night() < 0) {
            throw new IllegalArgumentException("Price per night must be greater than 0");
        }
        if(listing.getHost().getId() == null || listing.getHost().getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Host id cannot be empty");
        }
    }*/
    
    // limit what's shown when grabbing listings
    private ListingResponse convertToDTO(Listing listing) {
        ListingResponse listingResponse = new ListingResponse();
        
        listingResponse.setTitle(listing.getTitle());
        listingResponse.setDescription(listing.getDescription());
        listingResponse.setCapacity(listing.getCapacity());
        listingResponse.setPrice_per_night(listing.getPricePerNight());
        listingResponse.setUtilities(listing.getUtilities());
        listingResponse.setHost(listing.getHost().getUsername());
        
        return listingResponse;
    }
}
