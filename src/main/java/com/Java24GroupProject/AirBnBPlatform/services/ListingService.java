package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public ListingService(ListingRepository listingRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    public ListingResponse createListing(Listing listing) {
        if (listing.getTitle() == null || listing.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (listing.getPrice_per_night() == null || listing.getPrice_per_night().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than 0");
        }

        if (listing.getCapacity() <= 0) {
            throw new IllegalArgumentException("capacity must be greater than 0");
        }

        userRepository.findById(listing.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //save new listing
        listingRepository.save(listing);

        //return as ResponseDTO
        return convertToListingResponseDTO(listing);

    }

    //get all listings
    public List<ListingResponse> getAllListings() {
        List<Listing> listings = listingRepository.findAll();

        //convert Listing to ListingResponse
        return listings.stream()
                .map(this::convertToListingResponseDTO)
                .collect(Collectors.toList());
    }

    //get listing using listing id
    public ListingResponse getListingById(String id) {
        //validate listing id and get existing listing
        Listing listing = validateListingIdAndGetListing(id);

        //return as DTO
        return convertToListingResponseDTO(listing);
    }

    
    // PATCH
    public ListingResponse updateListing(String id, Listing listing) {
        //validate listing id and get existing listing
        Listing existingListing = validateListingIdAndGetListing(id);
       
        // only non null field will be updated
        if (listing.getTitle() != null) {
            existingListing.setTitle(listing.getTitle());
        }
        if (listing.getPrice_per_night() != null) {
            existingListing.setPrice_per_night(listing.getPrice_per_night());
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
        if(listing.getAvailableDates() != null) {
            existingListing.setAvailableDates(listing.getAvailableDates());
        }

        //update updatedAt
        listing.setUpdatedAt(LocalDateTime.now());

        //save updated listing
        listingRepository.save(existingListing);

        //return as ResponseDTO
        return convertToListingResponseDTO(existingListing);
    }

    //validate listing id exists in database and delete listing
    public void deleteListing(String id) {
        Listing listing = validateListingIdAndGetListing(id);
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
    private ListingResponse convertToListingResponseDTO(Listing listing) {
        User user = userRepository.findById(listing.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ListingResponse listingResponse = new ListingResponse();
        
        listingResponse.setTitle(listing.getTitle());
        listingResponse.setDescription(listing.getDescription());
        listingResponse.setCapacity(listing.getCapacity());
        listingResponse.setPrice_per_night(listing.getPrice_per_night());
        listingResponse.setUtilities(listing.getUtilities());
        listingResponse.setHost(user.getUsername());
        listingResponse.setAvailiableDates(listing.getAvailableDates());

        return listingResponse;
        
    }

    private Listing validateListingIdAndGetListing(String id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));

    }
}
