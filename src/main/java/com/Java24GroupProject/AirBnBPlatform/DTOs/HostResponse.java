package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.IdAndName;

import java.util.List;

public class HostResponse {
        private String id;
        private String username;
        private String profilePictureURL;
        private String description;
        private List<IdAndName> listings;

    public HostResponse(String id, String username, String profilePictureURL, String description, List<IdAndName> listings) {
        this.id = id;
        this.username = username;
        this.profilePictureURL = profilePictureURL;
        this.description = description;
        this.listings = listings;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public List<IdAndName> getListings() {
        return listings;
    }
}
