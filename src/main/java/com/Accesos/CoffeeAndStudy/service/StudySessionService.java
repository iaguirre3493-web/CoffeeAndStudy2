package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.domain.StudySession;
import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.PlaceRepository;
import com.Accesos.CoffeeAndStudy.repository.StudySessionRepository;
import com.Accesos.CoffeeAndStudy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudySessionService {

    private static final Logger logger = LoggerFactory.getLogger(StudySessionService.class);

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<StudySession> filterStudySessions(Long placeId, Long userId, Integer productivityLevel) {
        logger.info("Filtering study sessions with placeId={}, userId={}, productivityLevel={}", placeId, userId, productivityLevel);

        if (placeId != null && userId != null && productivityLevel != null) {
            return studySessionRepository.findByPlaceIdAndUserIdAndProductivityLevel(placeId, userId, productivityLevel);
        }
        if (placeId != null) {
            return studySessionRepository.findByPlaceId(placeId);
        }
        if (userId != null) {
            return studySessionRepository.findByUserId(userId);
        }
        if (productivityLevel != null) {
            return studySessionRepository.findByProductivityLevel(productivityLevel);
        }
        return studySessionRepository.findAll();
    }

    public StudySession getStudySessionById(Long id) {
        logger.info("Getting study session by id={}", id);

        return studySessionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("StudySession with id {} not found", id);
                    return new ResourceNotFoundException("StudySession with id " + id + " not found");
                });
    }

    public StudySession saveStudySession(StudySession studySession) {
        logger.info("Saving new study session");

        Long placeId = studySession.getPlace().getId();
        Long userId = studySession.getUser().getId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found for study session creation", placeId);
                    return new ResourceNotFoundException("Place with id " + placeId + " not found");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found for study session creation", userId);
                    return new ResourceNotFoundException("User with id " + userId + " not found");
                });

        studySession.setPlace(place);
        studySession.setUser(user);

        return studySessionRepository.save(studySession);
    }

    public StudySession updateStudySession(Long id, StudySession updatedStudySession) {
        logger.info("Updating study session with id={}", id);

        StudySession studySession = studySessionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("StudySession with id {} not found for update", id);
                    return new ResourceNotFoundException("StudySession with id " + id + " not found");
                });

        Long placeId = updatedStudySession.getPlace().getId();
        Long userId = updatedStudySession.getUser().getId();

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found for study session update", placeId);
                    return new ResourceNotFoundException("Place with id " + placeId + " not found");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found for study session update", userId);
                    return new ResourceNotFoundException("User with id " + userId + " not found");
                });

        studySession.setSessionDate(updatedStudySession.getSessionDate());
        studySession.setDurationMinutes(updatedStudySession.getDurationMinutes());
        studySession.setProductivityLevel(updatedStudySession.getProductivityLevel());
        studySession.setConsumedSomething(updatedStudySession.getConsumedSomething());
        studySession.setTotalSpent(updatedStudySession.getTotalSpent());
        studySession.setReservedTable(updatedStudySession.getReservedTable());
        studySession.setNotes(updatedStudySession.getNotes());
        studySession.setPlace(place);
        studySession.setUser(user);

        logger.info("Study session with id {} updated successfully", id);
        return studySessionRepository.save(studySession);
    }

    public void deleteStudySession(Long id) {
        logger.info("Deleting study session with id={}", id);

        StudySession studySession = studySessionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("StudySession with id {} not found for delete", id);
                    return new ResourceNotFoundException("StudySession with id " + id + " not found");
                });

        studySessionRepository.delete(studySession);
        logger.info("Study session with id {} deleted successfully", id);
    }
}