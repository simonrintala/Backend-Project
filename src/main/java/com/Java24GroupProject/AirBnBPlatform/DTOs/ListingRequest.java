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
    private String description;
    private double price_per_night;
    private Set<ListingUtilities> utilities;
    private int capacity;
    private List<User> hosts;
    
    public ListingRequest(String title, String description, double price_per_night, Set<ListingUtilities> utilities, int capacity, List<User> hosts) {
        this.title = title;
        this.description = description;
        this.price_per_night = price_per_night;
        this.utilities = utilities;
        this.capacity = capacity;
        this.hosts = hosts;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getPrice_per_night() {
        return price_per_night;
    }
    
    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public List<User> getHosts() {
        return hosts;
    }
}
