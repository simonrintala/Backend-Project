package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//The DTO for returning information at successful listing creation
public class ListingResponse {
    private String id;
    private String title;
    private String hostId;
    private String hostName;
    private String description;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private Set<ListingUtilities> utilities;
    private List<DateRange> availableDates;
    private String location;
    private List<String> image_urls;
    private double averageRating;

    public ListingResponse(String id, String title, String hostId, String hostName, String description, BigDecimal pricePerNight, Integer capacity, Set<ListingUtilities> utilities, List<DateRange> availableDates, String location, List<String> image_urls, double averageRating) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.hostName = hostName;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.utilities = utilities;
        this.availableDates = availableDates;
        this.location = location;
        this.image_urls = image_urls;
        this.averageRating = averageRating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getHostId() {
        return hostId;
    }

    public String getHostName() {
        return hostName;
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

    public List<String> getImage_urls() {
        return image_urls;
    }

    public double getAverageRating() {
        return averageRating;
    }
}
