package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.IdAndName;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

//The DTO for returning information at successful listing creation
public class ListingResponse {
    private String id;
    private String title;
    private IdAndName host;
    private String description;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private Set<ListingUtilities> utilities;
    private List<DateRange> availableDates;
    private String location;
    private List<String> imageUrls;
    private double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ListingResponse(String id, String title, String hostId, String hostName, String description, BigDecimal pricePerNight, Integer capacity, Set<ListingUtilities> utilities, List<DateRange> availableDates, String location, List<String> imageUrls, double averageRating, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.host = new IdAndName(hostId, hostName);
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.utilities = utilities;
        this.availableDates = availableDates;
        this.location = location;
        this.imageUrls = imageUrls;
        this.averageRating = averageRating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public IdAndName getHost() {
        return host;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Set<ListingUtilities> getUtilities() {
        return utilities;
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

    public double getAverageRating() {
        return averageRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
