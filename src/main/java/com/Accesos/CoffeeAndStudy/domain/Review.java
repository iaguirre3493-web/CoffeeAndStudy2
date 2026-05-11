package com.Accesos.CoffeeAndStudy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(max = 300)
    @Column(nullable = false, length = 300)
    private String comment;

    @NotNull
    private Boolean studyFriendly;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer noiseLevel;

    @NotNull
    private Boolean socketAvailability;

    private LocalDate reviewDate;

    @PositiveOrZero
    private Float recommendedHours;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getStudyFriendly() {
        return studyFriendly;
    }

    public void setStudyFriendly(Boolean studyFriendly) {
        this.studyFriendly = studyFriendly;
    }

    public Integer getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Integer noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Boolean getSocketAvailability() {
        return socketAvailability;
    }

    public void setSocketAvailability(Boolean socketAvailability) {
        this.socketAvailability = socketAvailability;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Float getRecommendedHours() {
        return recommendedHours;
    }

    public void setRecommendedHours(Float recommendedHours) {
        this.recommendedHours = recommendedHours;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}