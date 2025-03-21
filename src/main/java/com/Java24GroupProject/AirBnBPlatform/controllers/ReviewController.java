package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewResponse;
import com.Java24GroupProject.AirBnBPlatform.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Get all reviews for a specific listing
    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByListingId(@PathVariable String listingId) {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsByListing(listingId);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    // Create a new review
    @PreAuthorize("hasAnyRole('ADMIN','HOST','USER')")
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse reviewResponse = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.CREATED);
    }

    // Get all reviews for current user
    @PreAuthorize("hasAnyRole('ADMIN','HOST','USER')")
    @GetMapping("/user")
    public ResponseEntity<List<ReviewResponse>> getReviewsCurrentUser() {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsCurrentUser();
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HOST','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //ADMIN-specific endpoints ----------------------------------------------------------------------------

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable String userId) {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsByUserId(userId);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }


    // Get for avg rating and reviews based on host
    /* @GetMapping("/host/{hostId}/avgrating")
    public ResponseEntity<Double> getAverageRatingForHost(@PathVariable String hostId) {
        double averageRating = reviewService.getAverageRatingForHost(hostId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }
    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByHost(@PathVariable String hostId) {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsByHost(hostId);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }*/


}
