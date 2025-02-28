package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//The DTO for receiving information for listing creation
public class ListingRequest {
    @NotBlank
    private String title;
    @NotBlank
    private BigDecimal pricePerNight;
    private String description;
    private Set<ListingUtilities> utilities;
    private Integer capacity;
    private User host;
    private List<DateRange> availableDates;

    public ListingRequest(String title, BigDecimal pricePerNight, String description, Set<ListingUtilities> utilities, Integer capacity, User host, List<DateRange> availableDates) {
        this.title = title;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.utilities = utilities;
        this.capacity = capacity;
        this.host = host;
        this.availableDates = availableDates;
    }

    public @NotBlank String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public @NotBlank BigDecimal getPricePerNight() {
        return getPricePerNight();
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

    public List<DateRange> getAvailableDates() {
        return availableDates;
    }
}
