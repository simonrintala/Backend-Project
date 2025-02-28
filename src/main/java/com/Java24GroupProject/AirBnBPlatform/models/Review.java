package com.Java24GroupProject.AirBnBPlatform.models;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;

    @DBRef
    @NotNull(message = "A booking is required")
    private Booking booking;

    @NotNull(message = "A end date is required")
    private DateRange endDate;

    @Positive(message = "Review score must be greater than zero")
    @Min(value = 1, message = "Minimum value must be atleast 1")
    @Max(value = 5, message = "Maximum value cannot be more than 5")
    private Double rating;

    @CreatedDate
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(String id, Booking booking, DateRange endDate, Double rating, LocalDateTime createdAt) {
        this.id = id;
        this.booking = booking;
        this.endDate = endDate;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull(message = "A booking is required") Booking getBooking() {
        return booking;
    }

    public void setBooking(@NotNull(message = "A booking is required") Booking booking) {
        this.booking = booking;
    }

    public @Positive(message = "Review score must be greater than zero") @Min(value = 1, message = "Minimum value must be atleast 1") @Max(value = 5, message = "Maximum value cannot be more than 5") Double getRating() {
        return rating;
    }

    public void setRating(@Positive(message = "Review score must be greater than zero") @Min(value = 1, message = "Minimum value must be atleast 1") @Max(value = 5, message = "Maximum value cannot be more than 5") Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
