package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.domain.Review;
import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.PlaceRepository;
import com.Accesos.CoffeeAndStudy.repository.ReviewRepository;
import com.Accesos.CoffeeAndStudy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Review> filterReviews(Long placeId, Integer rating, Boolean studyFriendly) {
        logger.info("Filtering reviews with placeId={}, rating={}, studyFriendly={}", placeId, rating, studyFriendly);

        if (placeId != null && rating != null && studyFriendly != null) {
            return reviewRepository.findByPlaceIdAndRatingAndStudyFriendly(placeId, rating, studyFriendly);
        }
        if (placeId != null) {
            return reviewRepository.findByPlaceId(placeId);
        }
        if (rating != null) {
            return reviewRepository.findByRating(rating);
        }
        if (studyFriendly != null) {
            return reviewRepository.findByStudyFriendly(studyFriendly);
        }
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        logger.info("Getting review by id={}", id);

        return reviewRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Review with id {} not found", id);
                    return new ResourceNotFoundException("Review with id " + id + " not found");
                });
    }

    public Review saveReview(Review review) {
        logger.info("Saving new review");

        Long placeId = review.getPlace().getId();
        Long userId = review.getUser().getId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found for review creation", placeId);
                    return new ResourceNotFoundException("Place with id " + placeId + " not found");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found for review creation", userId);
                    return new ResourceNotFoundException("User with id " + userId + " not found");
                });

        review.setPlace(place);
        review.setUser(user);

        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review updatedReview) {
        logger.info("Updating review with id={}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Review with id {} not found for update", id);
                    return new ResourceNotFoundException("Review with id " + id + " not found");
                });

        Long placeId = updatedReview.getPlace().getId();
        Long userId = updatedReview.getUser().getId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found for review update", placeId);
                    return new ResourceNotFoundException("Place with id " + placeId + " not found");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found for review update", userId);
                    return new ResourceNotFoundException("User with id " + userId + " not found");
                });

        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());
        review.setStudyFriendly(updatedReview.getStudyFriendly());
        review.setNoiseLevel(updatedReview.getNoiseLevel());
        review.setSocketAvailability(updatedReview.getSocketAvailability());
        review.setReviewDate(updatedReview.getReviewDate());
        review.setRecommendedHours(updatedReview.getRecommendedHours());
        review.setPlace(place);
        review.setUser(user);

        logger.info("Review with id {} updated successfully", id);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        logger.info("Deleting review with id={}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Review with id {} not found for delete", id);
                    return new ResourceNotFoundException("Review with id " + id + " not found");
                });

        reviewRepository.delete(review);
        logger.info("Review with id {} deleted successfully", id);
    }
}