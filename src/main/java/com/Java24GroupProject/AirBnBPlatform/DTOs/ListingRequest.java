package com.Java24GroupProject.AirBnBPlatform.DTOs;

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
    private List<DateRange> availableDates;
    private String location;
    private List<String> imageUrls;

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

    public List<DateRange> getAvailableDates() {
        return availableDates;
    }
    
    public String getLocation() {
        return location;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
