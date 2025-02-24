package com.Java24GroupProject.AirBnBPlatform.models;


import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "listings")
public class Listing {
    @Id
    private String id;

    private String title;

    private String description;

    @NotNull(message = "A price per night is required.")
    @Positive(message = "Price per night must be greater than zero")
    private BigDecimal price_per_night;

    @NotNull(message = "Capacity limit must be set.")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    private Set<ListingUtilities> utilities;

    @NotNull(message = "Listing must have at least one host.")
    @DBRef
    private User host;

    private List<String> image_urls;

    // location by city so one can search by city later on
    private String location;

    // learn about something like updated_at
    @CreatedDate
    private LocalDateTime created_at;

    public Listing() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User hosts) {
        this.host = hosts;
    }

    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }

    public void setUtilities(Set<ListingUtilities> utilities) {
        this.utilities = utilities;
    }

    public List<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(List<String> image_urls) {
        this.image_urls = image_urls;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public BigDecimal getPrice_per_night() {
        return price_per_night;
    }

    public void setPrice_per_night(BigDecimal price_per_night) {
        this.price_per_night = price_per_night;
    }
}