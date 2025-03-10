package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewResponse;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ReviewRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ListingService listingService;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, BookingRepository bookingRepository, UserService userService, ListingService listingService, ListingRepository listingRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.listingService = listingService;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    // Create a review
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        // Get the logged in users username from the JWT token
        User currentUser = UserService.verifyAuthenticationAndExtractUser(userRepository);

        // Validate the listing id
        Listing listing = ListingService.validateListingIdAndGetListing(reviewRequest.getListingId(), listingRepository);

        // Check if the booking exists and the end date has passed
        Booking booking = bookingRepository.findByUserAndListing(currentUser, listing)
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
        review.setUser(currentUser);
        review.setRating(reviewRequest.getRating());
        review.setEndDate(booking.getBookingDates().getEndDate());

        // Save the review
        Review savedReview = reviewRepository.save(review);

        // listing avg ratings
        updateAverageListingRating(reviewRequest.getListingId());
        return mapToReviewResponse(savedReview);
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

    public List<ReviewResponse> getReviewsCurrentUser() {
        User user = UserService.verifyAuthenticationAndExtractUser(userRepository);

        return getUserReviews(user);
    }

    public List<ReviewResponse> getReviewsByUserId(String userId) {
        User user = UserService.validateUserIdAndReturnUser(userId, userRepository);

        return getUserReviews(user);
    }

    public void deleteReview(String reviewId) {
        // Check if the review exists
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        // Delete the review
        reviewRepository.delete(review);

        // Update the average rating for the listing and host
        updateAverageListingRating(review.getListing().getId());
        // Not used at the moment.
        // updateAverageHostRating(review.getListing().getHost().getId());
    }

    public double getAverageRatingForListing(String listingId) {
        Listing listing = ListingService.validateListingIdAndGetListing(listingId, listingRepository);
        return listing.getAverageRating();
    }

    private List<ReviewResponse> getUserReviews(User user) {
            // Fetch all reviews for the user
            List<Review> reviews = reviewRepository.findByUser_Id(user.getId());

            // Map the reviews to ReviewResponse DTOs
            return reviews.stream()
                    .map(this::mapToReviewResponse)
                    .collect(Collectors.toList());
             //same as getReviewsByListing
    }

    // Update the average rating of a listing
    private void updateAverageListingRating(String listingId) {
        List<Review> reviews = reviewRepository.findByListing_Id(listingId);

        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        Listing listing = ListingService.validateListingIdAndGetListing(listingId, listingRepository);
        listing.setAverageRating(averageRating);
        listing.setUpdatedAt(LocalDateTime.now());

        // Use updateListing method to update the existing listing
        listingRepository.save(listing);
    }

    // method to map Review to ReviewResponse
    private ReviewResponse mapToReviewResponse(Review review) {
        return new ReviewResponse(review.getId(),
                review.getListing().getId(),
                review.getUser().getId(),
                review.getUsername(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

 /*
    public double getAverageRatingForHost(String hostId) {
        User host = userService.getUserById(hostId);
        return host.getAverageRating();
    }
     public List<ReviewResponse> getReviewsByHost(String hostId) {
        System.out.println("Fetching reviews for host: " + hostId);

        // Fetch all reviews for listings owned by the host
        List<Review> reviews = reviewRepository.findByListing_Host_Id(hostId);

        System.out.println("Number of reviews found: " + reviews.size());

        // Map the reviews to ReviewResponse DTOs
        return reviews.stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }
      // Update the average rating of a host
    public void updateAverageHostRating(String hostId) {
        System.out.println("Fetching reviews for host: " + hostId);

        // Fetch all reviews for listings owned by the host
        List<Review> reviews = reviewRepository.findByListing_Host_Id(hostId);

        System.out.println("Number of reviews found: " + reviews.size());

        // Log the reviews
        for (Review review : reviews) {
            System.out.println("Review ID: " + review.getId() + ", Rating: " + review.getRating());
        }

        // Calculate the average rating
        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        System.out.println("Calculated average rating: " + averageRating);

        // Fetch the host
        User host = userService.getUserByIdReview(hostId);
        if (host == null) {
            throw new ResourceNotFoundException("Host not found with ID: " + hostId);
        }

        // Update the host's average rating
        host.setAverageRating(averageRating);
        userService.updateCurrentUser(host); // Save the updated host

        System.out.println("Updated average rating for host " + hostId + ": " + averageRating);
    }
  */
}
