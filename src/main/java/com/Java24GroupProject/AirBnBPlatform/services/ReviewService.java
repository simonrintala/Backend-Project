package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ReviewRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BookingRepository bookingRepository, ListingRepository listingRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
    }

    public  Review createReview (String bookingId, Double rating) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new RuntimeException("Booking not found");

        }
        Booking booking = optionalBooking.get();

        if (booking.getEndDate().after(new Date())) {
                throw new RuntimeException("You can only review after your stay is over.");
            }

        Review review = new Review();
        review.setBooking(booking);
        review.setEndDate(booking.getEndDate());
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        updateListingAverageRating(booking.getListing().getId());

        return savedReview;
        
    }
    /*public Review createReview(Review review) {
    //kan endast skapa om bokning är avslutad
        if (review.getEndDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("You can only review after your stay");
        }

        Optional<Listing> optionalListing = listingRepository.findById(review.getListing().getId());
        if (optionalListing.isEmpty()) {
            throw new RuntimeException("Listing not found");
        }

        Optional<User> optionalUser = userRepository.findById(review.getUser().getId());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        updateListingAverageRating(review.getListing().getId());

        return savedReview;
    }*/

    public List<Review> getReviewsByListing(String listingId) {
        return reviewRepository.findByListing_Id(listingId);
    }

    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByUser_Id(userId);
    }

    private void updateListingAverageRating(String listingId) {
        List<Review> reviews = reviewRepository.findByListing_Id(listingId);

        if (!reviews.isEmpty()) {
            double avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            listingRepository.updateAverageRating(listingId, avgRating);
        }
    }
}
