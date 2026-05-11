package com.Accesos.CoffeeAndStudy.repository;

import com.Accesos.CoffeeAndStudy.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRating(Integer rating);

    List<Review> findByStudyFriendly(Boolean studyFriendly);

    List<Review> findByPlaceId(Long placeId);

    List<Review> findByPlaceIdAndRatingAndStudyFriendly(Long placeId, Integer rating, Boolean studyFriendly);
}