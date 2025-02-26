package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReveiwController {
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestParam String bookingId, @RequestParam Double rating) {
        Review savedReview = reviewService.createReview(bookingId, rating);
        return ResponseEntity.ok(savedReview);
    }
    /*@PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.createReview(review);
        return ResponseEntity.ok(savedReview);
    }*/

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<Review>> getReviewsByListingId(@PathVariable String listingId) {
        List<Review> reviews = reviewService.getReviewsByListing(listingId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable String userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

}
