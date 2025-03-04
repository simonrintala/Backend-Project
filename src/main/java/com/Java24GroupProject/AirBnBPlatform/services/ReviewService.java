package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ReviewRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ListingService listingService;

    public ReviewService(ReviewRepository reviewRepository, BookingRepository bookingRepository, UserRepository userRepository, UserService userService, ListingService listingService) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.listingService = listingService;
    }

    // Create a review
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        // Get the logged in users username from the JWT token
        String loggedInUsername = getLoggedInUsername();

        // Fetch the logged in user by username
        User loggedInUser = userService.findByUsername(loggedInUsername);
        if (loggedInUser == null) {
            throw new ResourceNotFoundException("User not found with username: " + loggedInUsername);
        }

        // Validate the listing
        Listing listing = listingService.validateListingIdAndGetListing(reviewRequest.getListingId());
        if (listing == null) {
            throw new ResourceNotFoundException("Listing not found with ID: " + reviewRequest.getListingId());
        }

        // Validate the host
        User host = userService.getUserById(reviewRequest.getHostId());
        if (host == null) {
            throw new ResourceNotFoundException("Host not found with ID: " + reviewRequest.getHostId());
        }

        // Check if the booking exists and the end date has passed
        Booking booking = bookingRepository.findByUserAndListing(loggedInUser, listing)
                .orElseThrow(() -> new IllegalArgumentException("No booking found for this user and listing."));

        if (booking.getBookingDates().getEndDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot leave a review before the stay has ended.");
        }

        // Validate the rating
        if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Create the review
        Review review = new Review();
        review.setListing(listing);
        review.setUser(loggedInUser);
        review.setRating(reviewRequest.getRating());
        review.setEndDate(booking.getBookingDates().getEndDate());
        review.setCreatedAt(LocalDateTime.now());

        // Save the review
        Review savedReview = reviewRepository.save(review);

        // Update host and listing avg ratings
        updateAverageHostRating(reviewRequest.getHostId());
        updateAverageListingRating(reviewRequest.getListingId());

        ReviewResponse reviewResponse = mapToReviewResponse(savedReview);

        return reviewResponse;
    }

    // Update the average rating of a host
    public void updateAverageHostRating(String hostId) {
        List<Review> reviews = reviewRepository.findByListing_Host_Id(hostId);
        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        User host = userService.getUserById(hostId);
        host.setAverageRating(averageRating);
        userService.registerUser(host); // Save the updated user
    }

    // Update the average rating of a listing
    public void updateAverageListingRating(String listingId) {
        List<Review> reviews = reviewRepository.findByListing_Id(listingId);
        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        Listing listing = listingService.validateListingIdAndGetListing(listingId);
        listing.setAverageRating(averageRating);
        listingService.createListing(listing); // Save the updated listing
    }

    public List<ReviewResponse> getReviewsByListing(String listingId) {
        // Fetch all reviews for the listing
        List<Review> reviews = reviewRepository.findByListing_Id(listingId);

        // Update the average rating for the listing
        updateAverageListingRating(listingId);

        // Map the reviews to ReviewResponse DTOs
        return reviews.stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByUser(String userId) {
        // Fetch all reviews for the user
        List<Review> reviews = reviewRepository.findByUser_Id(userId);

        // Map the reviews to ReviewResponse DTOs
        return reviews.stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    } //same as getReviewsByListing

    public void deleteReview(String reviewId) {
        // Check if the review exists
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        // Delete the review
        reviewRepository.delete(review);

        // Update the average rating for the listing and host
        updateAverageListingRating(review.getListing().getId());
        updateAverageHostRating(review.getListing().getHost().getId());
    }

    public List<ReviewResponse> getReviewsByHost(String hostId) {
        // Fetch all reviews for listings owned by the host
        List<Review> reviews = reviewRepository.findByListing_Host_Id(hostId);

        // Map the reviews to ReviewResponse DTOs
        return reviews.stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    public double getAverageRatingForListing(String listingId) {
        Listing listing = listingService.validateListingIdAndGetListing(listingId);
        return listing.getAverageRating();
    }
    public double getAverageRatingForHost(String hostId) {
        User host = userService.getUserById(hostId);
        return host.getAverageRating();
    }

    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Extract username
        } else {
            throw new IllegalStateException("User not authenticated.");
        }
    }

    // method to map Review to ReviewResponse
    private ReviewResponse mapToReviewResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setTitle(review.getListing().getTitle());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setCreatedAt(review.getCreatedAt());
        return reviewResponse;
    }
}
