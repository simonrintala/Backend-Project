package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.User;
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
    private String host;
    private String location;
    private List<String> image_urls;
    
    public ListingRequest(String title, BigDecimal pricePerNight, String description, Set<ListingUtilities> utilities, Integer capacity, String host, String location, List<String> image_urls) {
        this.title = title;
        this.description = description;
        this.pricePerNight= pricePerNight;
        this.utilities = utilities;
        this.capacity = capacity;
        this.host = host;
        this.location = location;
        this.image_urls = image_urls;
    }
    
    public @NotBlank String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public String getHost() {
        return host;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public String getLocation() {
        return location;
    }
    
    public List<String> getImage_urls() {
        return image_urls;
    }
}
