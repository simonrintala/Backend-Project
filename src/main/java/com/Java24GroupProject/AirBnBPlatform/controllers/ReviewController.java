package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.ReviewResponse;
import com.Java24GroupProject.AirBnBPlatform.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Create a new review
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewResponse reviewResponse = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.CREATED);
    }

    // Get all reviews for a specific listing
    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByListing(@PathVariable String listingId) {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsByListing(listingId);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    // Get all reviews made by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable String userId) {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsByUser(userId);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByHost(@PathVariable String hostId) {
        List<ReviewResponse> reviewResponses = reviewService.getReviewsByHost(hostId);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listing/{listingId}/avgrating")
    public ResponseEntity<Double> getAverageRatingForListing(@PathVariable String listingId) {
        double averageRating = reviewService.getAverageRatingForListing(listingId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }

    @GetMapping("/host/{hostId}/avgrating")
    public ResponseEntity<Double> getAverageRatingForHost(@PathVariable String hostId) {
        double averageRating = reviewService.getAverageRatingForHost(hostId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }


}
