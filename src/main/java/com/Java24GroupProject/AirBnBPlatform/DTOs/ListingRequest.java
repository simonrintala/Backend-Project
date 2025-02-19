package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

//The DTO for receiving information for listing creation
public class ListingRequest {
    @NotBlank
    private String title;
    @NotBlank
    private Double price_per_night;
    private String description;
    private Set<ListingUtilities> utilities;
    private Integer capacity;
    private User host;
    
    public ListingRequest(String title, String description, Double price_per_night, Set<ListingUtilities> utilities, Integer capacity, User host) {
        this.title = title;
        this.description = description;
        this.price_per_night = price_per_night;
        this.utilities = utilities;
        this.capacity = capacity;
        this.host = host;
    }
    
    public @NotBlank String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public @NotBlank Double getPrice_per_night() {
        return price_per_night;
    }
    
    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public User getHost() {
        return host;
    }
}
