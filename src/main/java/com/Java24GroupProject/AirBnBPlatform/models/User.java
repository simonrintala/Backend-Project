package com.Java24GroupProject.AirBnBPlatform.models;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotNull(message = "username is a required field")
    @NotEmpty(message = "username is a required field")
    @NotBlank(message = "username is a required field")
    @Indexed(unique = true)
    private String username;

    @NotNull(message = "password is a required field")
    @NotEmpty(message = "password is a required field")
    @NotBlank(message = "password is a required field")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
            message = "password must be minimum 8 characters and must contain must contain: 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character")
    private String password;

    @NotNull(message = "email is a required field")
    @NotEmpty(message = "email is a required field")
    @NotBlank(message = "email is a required field")
    @Email(message = "email does not have a valid format")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "phoneNr is a required field")
    @NotEmpty(message = "phoneNr is a required field")
    @NotBlank(message = "phoneNr is a required field")
    @Pattern(regexp = "^[0-9+]+$", message = "phoneNr may only contain numbers and +")
    @Indexed(unique = true)
    private String phoneNr;

    private UserAddress address;

    private String profilePictureURL;

    private String description;


    @DBRef
    private List<Listing> favorites;

    private Set<Role> roles;

    private Double averageRating;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public User() {
    }

    public String getId() {
        return id;
    }

    public @NotNull(message = "username is a required field") @NotEmpty(message = "username is a required field") @NotBlank(message = "username is a required field") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "username is a required field") @NotEmpty(message = "username is a required field") @NotBlank(message = "username is a required field") String username) {
        this.username = username;
    }

    public @NotNull(message = "password is a required field") @NotEmpty(message = "password is a required field") @NotBlank(message = "password is a required field") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
            message = "password must be minimum 8 characters and must contain must contain: 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "password is a required field") @NotEmpty(message = "password is a required field") @NotBlank(message = "password is a required field") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
            message = "password must be minimum 8 characters and must contain must contain: 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character") String password) {
        this.password = password;
    }

    public @NotNull(message = "email is a required field") @NotEmpty(message = "email is a required field") @NotBlank(message = "email is a required field") @Email(message = "email does not have a valid format") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "email is a required field") @NotEmpty(message = "email is a required field") @NotBlank(message = "email is a required field") @Email(message = "email does not have a valid format") String email) {
        this.email = email;
    }

    public @NotNull(message = "phoneNr is a required field") @NotEmpty(message = "phoneNr is a required field") @NotBlank(message = "phoneNr is a required field") @Pattern(regexp = "^[0-9+]+$", message = "phoneNr may only contain numbers and +") String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(@NotNull(message = "phoneNr is a required field") @NotEmpty(message = "phoneNr is a required field") @NotBlank(message = "phoneNr is a required field") @Pattern(regexp = "^[0-9+]+$", message = "phoneNr may only contain numbers and +") String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Listing> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Listing> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(Listing listing) {
        favorites.add(listing);
    }

    public void removeFavorite(Listing listing) {
        favorites.remove(listing);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


