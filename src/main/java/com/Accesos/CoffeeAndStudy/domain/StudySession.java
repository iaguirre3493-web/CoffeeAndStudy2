package com.Accesos.CoffeeAndStudy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "study_sessions")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate sessionDate;

    @NotNull
    @Positive
    private Integer durationMinutes;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer productivityLevel;

    @NotNull
    private Boolean consumedSomething;

    @PositiveOrZero
    private Float totalSpent;

    @NotNull
    private Boolean reservedTable;

    @Size(max = 300)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public StudySession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getProductivityLevel() {
        return productivityLevel;
    }

    public void setProductivityLevel(Integer productivityLevel) {
        this.productivityLevel = productivityLevel;
    }

    public Boolean getConsumedSomething() {
        return consumedSomething;
    }

    public void setConsumedSomething(Boolean consumedSomething) {
        this.consumedSomething = consumedSomething;
    }

    public Float getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Float totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Boolean getReservedTable() {
        return reservedTable;
    }

    public void setReservedTable(Boolean reservedTable) {
        this.reservedTable = reservedTable;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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