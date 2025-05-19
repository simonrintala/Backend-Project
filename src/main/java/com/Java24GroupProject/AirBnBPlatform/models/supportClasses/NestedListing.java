package com.Java24GroupProject.AirBnBPlatform.models.supportClasses;

import java.util.List;

public record NestedListing(String id, String title, String location, List<String> imageUrls) {
}
