package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.domain.Review;
import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.GlobalExceptionHandler;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(reviewController, "reviewService", reviewService);

        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldReturnAllReviews() throws Exception {
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

        when(reviewService.filterReviews(null, null, null)).thenReturn(List.of(review));

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Muy buen sitio para estudiar"));
    }

    @Test
    void shouldReturnReviewById() throws Exception {
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

        when(reviewService.getReviewById(1L)).thenReturn(review);

        mockMvc.perform(get("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void shouldReturn404WhenReviewNotFound() throws Exception {
        when(reviewService.getReviewById(99L))
                .thenThrow(new ResourceNotFoundException("Review with id 99 not found"));

        mockMvc.perform(get("/reviews/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateReview() throws Exception {
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

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setRating(5);
        savedReview.setComment("Muy buen sitio para estudiar");
        savedReview.setStudyFriendly(true);
        savedReview.setNoiseLevel(2);
        savedReview.setSocketAvailability(true);
        savedReview.setReviewDate(LocalDate.of(2025, 10, 5));
        savedReview.setRecommendedHours(3.5f);
        savedReview.setPlace(place);
        savedReview.setUser(user);

        when(reviewService.saveReview(any(Review.class))).thenReturn(savedReview);

        mockMvc.perform(post("/reviews")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void shouldUpdateReview() throws Exception {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setRating(4);
        review.setComment("Nuevo comentario");
        review.setStudyFriendly(true);
        review.setNoiseLevel(3);
        review.setSocketAvailability(true);
        review.setReviewDate(LocalDate.of(2025, 10, 6));
        review.setRecommendedHours(2.5f);
        review.setPlace(place);
        review.setUser(user);

        Review updatedReview = new Review();
        updatedReview.setId(1L);
        updatedReview.setRating(4);
        updatedReview.setComment("Nuevo comentario");
        updatedReview.setStudyFriendly(true);
        updatedReview.setNoiseLevel(3);
        updatedReview.setSocketAvailability(true);
        updatedReview.setReviewDate(LocalDate.of(2025, 10, 6));
        updatedReview.setRecommendedHours(2.5f);
        updatedReview.setPlace(place);
        updatedReview.setUser(user);

        when(reviewService.updateReview(eq(1L), any(Review.class))).thenReturn(updatedReview);

        mockMvc.perform(put("/reviews/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Nuevo comentario"));
    }

    @Test
    void shouldDeleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreatingInvalidReview() throws Exception {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        Review invalidReview = new Review();
        invalidReview.setRating(0);
        invalidReview.setComment("");
        invalidReview.setStudyFriendly(true);
        invalidReview.setNoiseLevel(2);
        invalidReview.setSocketAvailability(true);
        invalidReview.setReviewDate(LocalDate.of(2025, 10, 5));
        invalidReview.setRecommendedHours(3.5f);
        invalidReview.setPlace(place);
        invalidReview.setUser(user);

        mockMvc.perform(post("/reviews")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReview)))
                .andExpect(status().isBadRequest());
    }
}