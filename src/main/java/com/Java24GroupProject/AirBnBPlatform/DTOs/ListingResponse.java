package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;


import java.util.Set;

//The DTO for returning information at successful listing creation
public class ListingResponse {
    private String title;
    private String description;
    private Double price_per_night;
    private Integer capacity;
    
    public ListingResponse(String title, String description, Double price_per_night, Integer capacity) {
        this.title = title;
        this.description = description;
        this.price_per_night = price_per_night;
        this.capacity = capacity;
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
    
    public Double getPrice_per_night() {
        return price_per_night;
    }
    
    public void setPrice_per_night(Double price_per_night) {
        this.price_per_night = price_per_night;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
