package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public ListingService(ListingRepository listingRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    public ListingResponse createListing(ListingRequest listingRequest) {
        // validate listing data
        validateListing(listingRequest);

        //convert from RequestDTO to Listing
        Listing listing = convertRequestToListing(listingRequest);
        //save new listing
        listingRepository.save(listing);

        //return as ResponseDTO
        return convertToListingResponseDTO(listing);

    }

    //get all listings
    public List<ListingResponse> getAllListings() {
        List<ListingResponse> listingResponses = new ArrayList<>();
        
        // convert Listing to ListingResponseDTO
        for (Listing listing : listingRepository.findAll()) {
            listingResponses.add(convertToListingResponseDTO(listing));
        }
        return listingResponses;
    }
    
    //get listing by id
    public ListingResponse getListingById(String id) {
        Listing listing = validateListingIdAndGetListing(id);
        
        return convertToListingResponseDTO(listing);
    }
    
    //get all listings users by hosts id
    public List<ListingResponse> getAllListingsByHostId(String id) {
        //check if user is valid
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        //convert to DTO
        List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingRepository.findByHostId(user.getId())) {
            listingResponses.add(convertToListingResponseDTO(listing));
        }
        return listingResponses;
    }
    
    // get listings by price interval
    public List<ListingResponse> getListingByPriceRange(double minPrice, double maxPrice) {
        // make sure none of the prices are negative
        if (minPrice < 0 || maxPrice <= 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        // make sure minPrice is not greater that maxPrice
        if (minPrice > maxPrice ) {
            throw new IllegalArgumentException("Price cannot be greater than maxPrice");
        }

        //convert to DTO
        List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingRepository.findByPricePerNightBetween(minPrice, maxPrice)) {
            listingResponses.add(convertToListingResponseDTO(listing));
        }
        return listingResponses;
    }
    
    //get listings by location
    public List<ListingResponse> getListingByLocation(String location) {
        // make sure location isn't empty/null
        if(location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty or null");
        }
        
        //convert to DTO
        List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingRepository.findByLocation(location)) {
            listingResponses.add(convertToListingResponseDTO(listing));
        }
        return listingResponses;
    }
    
    //get listings by capacity interval
    public List<ListingResponse> getListingByCapacity(double minCapacity, double maxCapacity) {
        //checks so capacity isn't negative
        if (minCapacity < 0 || maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        //checks if min isn't greater or equal to max capacity
        if (minCapacity > maxCapacity) {
            throw new IllegalArgumentException("minCapacity cannot be greater than maxCapacity");
        }
        
        List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingRepository.findByCapacityBetween(minCapacity, maxCapacity)) {
            listingResponses.add(convertToListingResponseDTO(listing));
        }
        return listingResponses;
    }
    
    //get listing by utilities
    public List<ListingResponse> getListingByUtilities(String utility) {
        //make sure utility isn't empty
        if(utility == null || utility.isEmpty()) {
            throw new IllegalArgumentException("Utility cannot be empty or null");
        }
        
        //convert to DTO
        List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingRepository.findByUtilities(utility)) {
            listingResponses.add(convertToListingResponseDTO(listing));
        }
        return listingResponses;
    }

    
    // PATCH
    public ListingResponse updateListing(String id, Listing listing) {
        //validate listing id and get existing listing
        Listing existingListing = validateListingIdAndGetListing(id);
       
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
    
    // limit what's shown when grabbing listings
    private ListingResponse convertToListingResponseDTO(Listing listing) {
        User user = userRepository.findById(listing.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ListingResponse listingResponse = new ListingResponse();
        
        listingResponse.setTitle(listing.getTitle());
        listingResponse.setDescription(listing.getDescription());
        listingResponse.setCapacity(listing.getCapacity());
        listingResponse.setPricePerNight(listing.getPricePerNight());
        listingResponse.setUtilities(listing.getUtilities());
        listingResponse.setHost(user.getUsername());
        listingResponse.setAvailiableDates(listing.getAvailableDates());

        return listingResponse;
        
    }
    
    //convert ListingRequest to Listing
    public Listing convertRequestToListing(ListingRequest listingRequest) {
        Listing listing = new Listing();
        listing.setTitle(listingRequest.getTitle());
        listing.setDescription(listingRequest.getDescription());
        listing.setCapacity(listingRequest.getCapacity());
        listing.setPricePerNight(listingRequest.getPricePerNight());
        listing.setUtilities(listingRequest.getUtilities());
        listing.setHost(listingRequest.getHost());
        listing.setAvailableDates(listingRequest.getAvailableDates());
        return listing;
    }
    
    private void validateListing(ListingRequest listingRequest) {
        // check if userId for host is valid
        userRepository.findById(listingRequest.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // check if utilities exists
        
        // check if title is empty/null
        if (listingRequest.getTitle() == null || listingRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        // check if pricePerNight is empty/null
        if (listingRequest.getPricePerNight() == null || listingRequest.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than 0");
        }
        //check if capacity is greater than 0
        if (listingRequest.getCapacity() <= 0) {
            throw new IllegalArgumentException("capacity must be greater than 0");
        }
    }
    
    private Listing validateListingIdAndGetListing(String id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
        
    }
    //validate user id and get user object from bookingRequest
    private User validateUserIdAndGetUser(ListingRequest listingRequest) {
        return userRepository.findById(listingRequest.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
    }
}
