package com.Accesos.CoffeeAndStudy.repository;

import com.Accesos.CoffeeAndStudy.domain.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByPlaceId(Long placeId);

    List<StudySession> findByUserId(Long userId);

    List<StudySession> findByProductivityLevel(Integer productivityLevel);

    List<StudySession> findByPlaceIdAndUserIdAndProductivityLevel(Long placeId, Long userId, Integer productivityLevel);
}