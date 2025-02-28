package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Review;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ReviewRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        DateRange dateRange = booking.getBookingDates();
        LocalDate endDate = dateRange.getEndDate();

        if (endDate.isAfter(LocalDate.now())) {
                throw new RuntimeException("You can only review after your stay is over.");
            }
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating should be between 1 and 5");
        } //dubbel med annoteringarna i models?

        Review review = new Review();
        review.setBooking(booking);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        updateListingAverageRating(booking.getListing().getHost().getId());

        return savedReview;
        
    }

    public List<Review> getReviewsByListing(String listingId) {
        return reviewRepository.findByBooking_Listing_Id(listingId);
    }

    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByBooking_User_Id(userId);
    }

    private void updateListingAverageRating(String listingId) {
        List<Review> reviews = reviewRepository.findByBooking_Listing_Id(listingId);

        if (!reviews.isEmpty()) {
            double avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            listingRepository.updateAverageRating(listingId, avgRating);
        }
    }

    private void updateHostAverageRating(String hostId) {
        List<Review> reviews = reviewRepository.findByBooking_Listing_Host_Id(hostId);

        if (!reviews.isEmpty()) {
            double avgRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            userRepository.updateAverageRating(hostId, avgRating);
        }
    }
}
