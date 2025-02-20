package com.Java24GroupProject.AirBnBPlatform.models;


import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    @DBRef
    @NotNull(message = "A listing ID is required")
    private Listing listing;

    @DBRef
    @NotNull(message = "A user is required")
    private User user;

//    @DBRef
//    private User username

//    private TimePeriod startDate;
//    private TimePeriod endDate;

    private Date startDate;
    private Date endDate;

    @NotNull(message = "Number of guests is required")
    @Positive(message = "Amount of guests must be greater than 0")
    private Integer numberOfGuests;

    @NotNull(message = "A total price is required")
    @Positive(message = "Total price must be greater than 0")
    private BigDecimal totalPrice;
    // (number of days) * (price per night)

    private BookingStatus bookingStatus;

    @CreatedDate
    private LocalDateTime createdDate;

    public Booking() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull(message = "A listing ID is required") Listing getListing() {
        return listing;
    }

    public void setListing(@NotNull(message = "A listing ID is required") Listing listing) {
        this.listing = listing;
    }

    public @NotNull(message = "A user is required") User getUser() {
        return user;
    }

    public void setUser(@NotNull(message = "A user is required") User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public @NotNull(message = "Number of guests is required") @Positive(message = "Amount of guests must be greater than 0") Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(@NotNull(message = "Number of guests is required") @Positive(message = "Amount of guests must be greater than 0") Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public @NotNull(message = "A total price is required") @Positive(message = "Total price must be greater than 0") BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(@NotNull(message = "A total price is required") @Positive(message = "Total price must be greater than 0") BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
