package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//The DTO for returning information at successful listing creation
public class ListingResponse {
    private String title;
    private String description;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private Set<ListingUtilities> utilities;
    private List<DateRange> availiableDates;
    private String host;
    
    public ListingResponse() {
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }
    
    public void setUtilities(Set<ListingUtilities> utilities) {
        this.utilities = utilities;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public List<DateRange> getAvailableDates() {
        return availiableDates;
    }

    public void setAvailiableDates(List<DateRange> availiableDates) {
        this.availiableDates = availiableDates;
    }
}
