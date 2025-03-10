package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ListingResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.UnauthorizedException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

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

    //METHODS used by LISTING CONTROLLER CLASS -----------------------------------------------------------------------

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
        Listing listing = validateListingIdAndGetListing(id, listingRepository);

        return convertToListingResponseDTO(listing);
    }

    //get all listings for a host, using hosts id
    public List<ListingResponse> getListingsByHostId(String hostId) {
        //check if user is valid
        User user = UserService.validateUserIdAndReturnUser(hostId, userRepository);
        return getListingsByUser(user);
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

    //create new listing with current user as host
    public ListingResponse createListing(ListingRequest listingRequest) {

        //convert from RequestDTO to Listing
        Listing listing = convertRequestToListing(listingRequest);

        //save new listing
        listing.setAverageRating(0D);
        listingRepository.save(listing);

        //return as ResponseDTO
        return convertToListingResponseDTO(listing);
    }

    //get all listings for the current user
    public List<ListingResponse> getListingsCurrentUser() {
        //get current user
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        return getListingsByUser(currentUser);
    }

    //update a listing, only the host of the listing can update a listing
    public ListingResponse updateListing(String id, ListingRequest listingRequest) {
        //validate listing id and get existing listing
        Listing existingListing = validateListingIdAndGetListing(id, listingRepository);

        //validate that the user is host of the listing
        String currentUserId = UserService.verifyAuthenticationAndExtractUser(userRepository).getId();
        if (!currentUserId.equals(existingListing.getHost().getId())) {
            throw new UnauthorizedException("Listing cannot be updated by current user.\n Only the listing can host update a listing.");
        }

        existingListing.setTitle(listingRequest.getTitle());
        existingListing.setDescription(listingRequest.getDescription());
        existingListing.setPricePerNight(listingRequest.getPricePerNight());
        existingListing.setCapacity(listingRequest.getCapacity());
        existingListing.setUtilities(listingRequest.getUtilities());
        existingListing.setLocation(listingRequest.getLocation());
        existingListing.setImageUrls(listingRequest.getImageUrls());
        existingListing.setAvailableDates(listingRequest.getAvailableDates());

        //save updated listing
        existingListing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(existingListing);

        //return as ResponseDTO
        return convertToListingResponseDTO(existingListing);
    }

    //validate listing id exists in database and delete listing and bookings for the listing
    //must be host of the listing or admin to delete a listings
    public void deleteListing(String id) {
        Listing listing = validateListingIdAndGetListing(id, listingRepository);

        //validate that the user is host of the listing or admin
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        if (!currentUser.getId().equals(listing.getHost().getId()) && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new UnauthorizedException("Listing cannot be deleted by current user.\n Only the listing host or an admin user can delete a listing.");
        }

        bookingRepository.deleteByListing(listing);
        listingRepository.delete(listing);
    }


    //METHODS used by this or other SERVICE CLASSES --------------------------------------------------------------

    // limit what's shown when grabbing listings
    private ListingResponse convertToListingResponseDTO(Listing listing) {
        
        return new ListingResponse(
                listing.getId(),
                listing.getTitle(),
                listing.getHost().getId(),
                listing.getHostName(),
                listing.getDescription(),
                listing.getPricePerNight(),
                listing.getCapacity(),
                listing.getUtilities(),
                listing.getAvailableDates(),
                listing.getLocation(),
                listing.getImageUrls(),
                listing.getAverageRating(),
                listing.getCreatedAt(),
                listing.getUpdatedAt()
        );
    }

    //convert ListingRequest to Listing
    private Listing convertRequestToListing(ListingRequest listingRequest) {
        // Create a new Listing object
        Listing listing = new Listing();

        // Set the host the current user
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);
        listing.setHost(currentUser);
        listing.setHostName(currentUser.getUsername());
        
        // Set fields from ListingRequest into Listing
        listing.setTitle(listingRequest.getTitle());
        listing.setDescription(listingRequest.getDescription());
        listing.setPricePerNight(listingRequest.getPricePerNight());
        listing.setCapacity(listingRequest.getCapacity());
        listing.setUtilities(listingRequest.getUtilities());
        listing.setAvailableDates(listingRequest.getAvailableDates());
        listing.setLocation(listingRequest.getLocation());
        listing.setImageUrls(listingRequest.getImageUrls());

        return listing;
    }

    //used by get listings for current user and for any user methods in this class
    private List<ListingResponse> getListingsByUser(User user) {
    //convert to DTO
    List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingRepository.findByHostId(user.getId())) {
        listingResponses.add(convertToListingResponseDTO(listing));
    }
        return listingResponses;
}

    public static Listing validateListingIdAndGetListing(String id, ListingRepository listingRepository) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing with id "+ id +" not in database"));

    }

}
