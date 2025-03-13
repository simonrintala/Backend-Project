package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//The DTO for receiving information for listing creation
public class ListingRequest {

    @NotNull(message = "title is a required field")
    @NotEmpty(message = "title is a required field")
    @NotBlank(message = "title is a required field")
    private String title;

    @NotNull(message = "pricePerNight is a required field")
    @Positive(message = "pricePerNight must be greater than zero")
    private BigDecimal pricePerNight;

    private String description;
    private Set<ListingUtilities> utilities;

    @NotNull(message = "capacity is a required field")
    @Positive(message = "capacity must be greater than zero")
    private Integer capacity;

    private List<DateRange> availableDates;

    @NotNull(message = "location is a required field")
    @NotEmpty(message = "location is a required field")
    @NotBlank(message = "location is a required field")
    private String location;
    private List<String> imageUrls;

    public ListingRequest() {
    }

    public @NotNull(message = "title is a required field") @NotEmpty(message = "title is a required field") @NotBlank(message = "title is a required field") String getTitle() {
        return title;
    }

    public void setTitle(@NotNull(message = "title is a required field") @NotEmpty(message = "title is a required field") @NotBlank(message = "title is a required field") String title) {
        this.title = title;
    }

    public @NotNull(message = "pricePerNight is a required field") @Positive(message = "pricePerNight must be greater than zero") BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(@NotNull(message = "pricePerNight is a required field") @Positive(message = "pricePerNight must be greater than zero") BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }

    public void setUtilities(Set<ListingUtilities> utilities) {
        this.utilities = utilities;
    }

    public @NotNull(message = "capacity is a required field") @Positive(message = "capacity must be greater than zero") Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(@NotNull(message = "capacity is a required field") @Positive(message = "capacity must be greater than zero") Integer capacity) {
        this.capacity = capacity;
    }

    public List<DateRange> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<DateRange> availableDates) {
        this.availableDates = availableDates;
    }

    public @NotNull(message = "location is a required field") @NotEmpty(message = "location is a required field") @NotBlank(message = "location is a required field") String getLocation() {
        return location;
    }

    public void setLocation(@NotNull(message = "location is a required field") @NotEmpty(message = "location is a required field") @NotBlank(message = "location is a required field") String location) {
        this.location = location;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
