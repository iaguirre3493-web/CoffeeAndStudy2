package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.domain.Review;
import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.PlaceRepository;
import com.Accesos.CoffeeAndStudy.repository.ReviewRepository;
import com.Accesos.CoffeeAndStudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void shouldReturnAllReviews() {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setComment("Muy buen sitio para estudiar");
        review.setStudyFriendly(true);
        review.setNoiseLevel(2);
        review.setSocketAvailability(true);
        review.setReviewDate(LocalDate.of(2025, 10, 5));
        review.setRecommendedHours(3.5f);
        review.setPlace(place);
        review.setUser(user);

        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<Review> result = reviewService.filterReviews(null, null, null);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getRating());
    }

    @Test
    void shouldReturnReviewById() {
        Review review = new Review();
        review.setId(1L);
        review.setComment("Comentario");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Review result = reviewService.getReviewById(1L);

        assertEquals("Comentario", result.getComment());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenReviewNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewById(99L));
    }

    @Test
    void shouldSaveReview() {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setRating(5);
        review.setComment("Muy buen sitio para estudiar");
        review.setStudyFriendly(true);
        review.setNoiseLevel(2);
        review.setSocketAvailability(true);
        review.setReviewDate(LocalDate.of(2025, 10, 5));
        review.setRecommendedHours(3.5f);
        review.setPlace(place);
        review.setUser(user);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.saveReview(review);

        assertEquals(5, result.getRating());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void shouldUpdateReview() {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        Review existingReview = new Review();
        existingReview.setId(1L);
        existingReview.setComment("Antiguo");

        Review updatedReview = new Review();
        updatedReview.setRating(4);
        updatedReview.setComment("Nuevo comentario");
        updatedReview.setStudyFriendly(true);
        updatedReview.setNoiseLevel(3);
        updatedReview.setSocketAvailability(true);
        updatedReview.setReviewDate(LocalDate.of(2025, 10, 6));
        updatedReview.setRecommendedHours(2.5f);
        updatedReview.setPlace(place);
        updatedReview.setUser(user);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);

        Review result = reviewService.updateReview(1L, updatedReview);

        assertEquals("Nuevo comentario", result.getComment());
        assertEquals(4, result.getRating());
    }

    @Test
    void shouldDeleteReview() {
        Review review = new Review();
        review.setId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).delete(review);
    }
}