package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
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
    private final BookingRepository bookingRepository;

    public ListingService(ListingRepository listingRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public ListingResponse createListing(ListingRequest listingRequest) {
        // validate listing data
        validateListing(listingRequest);

        //convert from RequestDTO to Listing
        Listing listing = convertRequestToListing(listingRequest);
        //save new listing
        
        listing.setUpdatedAt(null);
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

    
    // PUT
    public ListingResponse updateListing(String id, Listing listing) {
        //validate listing id and get existing listing
        Listing existingListing = validateListingIdAndGetListing(id);
        
        existingListing.setTitle(listing.getTitle());
        existingListing.setDescription(listing.getDescription());
        existingListing.setPricePerNight(listing.getPricePerNight());
        existingListing.setCapacity(listing.getCapacity());
        existingListing.setUtilities(listing.getUtilities());
        existingListing.setLocation(listing.getLocation());
        existingListing.setImage_urls(listing.getImage_urls());

        //save updated listing
        existingListing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(existingListing);

        //return as ResponseDTO
        return convertToListingResponseDTO(existingListing);
    }

    //validate listing id exists in database and delete listing and bookings for the listing
    public void deleteListing(String id) {
        Listing listing = validateListingIdAndGetListing(id);
        bookingRepository.deleteByListing(listing);
        listingRepository.delete(listing);
    }
    
    // limit what's shown when grabbing listings
    private ListingResponse convertToListingResponseDTO(Listing listing) {
        // Retrieve the host (user) based on the host ID in the listing
        User user = userRepository.findById(listing.getHost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return new ListingResponse(
                listing.getTitle(),
                listing.getDescription(),
                listing.getPricePerNight(),
                listing.getCapacity(),
                listing.getUtilities(),
                listing.getAvailableDates(),
                user.getUsername(),
                listing.getLocation(),
                listing.getImage_urls()
        );
    }
    
    
    //convert ListingRequest to Listing
    public Listing convertRequestToListing(ListingRequest listingRequest) {
        // Create a new Listing object
        Listing listing = new Listing();
        
        // Set fields from ListingRequest into Listing
        listing.setTitle(listingRequest.getTitle());
        listing.setDescription(listingRequest.getDescription());
        listing.setPricePerNight(listingRequest.getPricePerNight());
        listing.setCapacity(listingRequest.getCapacity());
        listing.setUtilities(listingRequest.getUtilities());
        listing.setAvailableDates(listingRequest.getAvailableDates());
        listing.setLocation(listingRequest.getLocation());
        
        // Set the host (the user creating the listing)
        listing.setHost(listingRequest.getHost());
        
        // set location and image URLs if provided in ListingRequest
        //listing.setLocation(listingRequest.getLocation());  // Make sure to add a location field in ListingRequest if needed
        //listing.setImageUrls(listingRequest.getImageUrls());  // Same goes for image URLs
        
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

}
