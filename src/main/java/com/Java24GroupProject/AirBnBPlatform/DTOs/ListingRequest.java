package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//The DTO for receiving information for listing creation
public class ListingRequest {
 
    private String title;
    private BigDecimal pricePerNight;
    private String description;
    private Set<ListingUtilities> utilities;
    private Integer capacity;
    private User host;
    private List<DateRange> availableDates;

    public ListingRequest() {
    }

    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
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
