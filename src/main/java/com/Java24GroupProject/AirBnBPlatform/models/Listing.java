package com.Java24GroupProject.AirBnBPlatform.models;


import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

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
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal pricePerNight;

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

    private List<DateRange> availableDates;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public List<DateRange> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<DateRange> availableDates) {
        this.availableDates = availableDates;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addAvailableDateRange(DateRange dateRange) {

        DateRange startsWhereNewDateRangeEnds = null;
        DateRange endWhereNewDateRangeStarts = null;

        for (DateRange availableDateRange : availableDates) {
            //check if there is overlap with existing dates
            if (dateRange.hasOverlapWithAnotherDateRange(availableDateRange)) {
                throw new IllegalArgumentException("dates could not be added, as they overlap with existing available date ranges");

                //if dates are identical to dates in list throw error
            } else if (dateRange.isIdenticalToAnotherDateRange(availableDateRange)) {
                throw new IllegalArgumentException("dates could not be added, already in available dates for listing");

                //does dateRange start at the end of existing date range
            } else if (dateRange.getStartDate().isEqual(availableDateRange.getEndDate())) {
                endWhereNewDateRangeStarts = availableDateRange;

                //does dateRange end at the start of existing date range
            } else if (availableDateRange.getStartDate().isEqual(dateRange.getEndDate())) {
                startsWhereNewDateRangeEnds = availableDateRange;
            }
        }

        //if new dateRange is not adjacent to any exising date range
        if (startsWhereNewDateRangeEnds == null && endWhereNewDateRangeStarts == null) {
            availableDates.add(dateRange);

            //if new daterange ends at existing daterange start date, extend existing adjacent daterange
        } else if (startsWhereNewDateRangeEnds != null && endWhereNewDateRangeStarts == null) {
            startsWhereNewDateRangeEnds.setStartDate(dateRange.getStartDate());
            //if new daterange starts at existing daterange end date, extend existing adjacent daterange
        } else if (startsWhereNewDateRangeEnds == null && endWhereNewDateRangeStarts != null) {
            endWhereNewDateRangeStarts.setEndDate(dateRange.getEndDate());
        //if it is adjacent to two existing dateranges
        } else {
            endWhereNewDateRangeStarts.setEndDate(startsWhereNewDateRangeEnds.getEndDate());
            availableDates.remove(startsWhereNewDateRangeEnds);
        }
    }
}