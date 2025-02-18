package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;


import java.util.Set;

//The DTO for returning information at successful listing creation
public class ListingResponse {
    private String title;
    private String description;
    private double price_per_night;
    private int capacity;
    
    public ListingResponse(String title, String description, double price_per_night, int capacity) {
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
    
    public double getPrice_per_night() {
        return price_per_night;
    }
    
    public void setPrice_per_night(double price_per_night) {
        this.price_per_night = price_per_night;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
