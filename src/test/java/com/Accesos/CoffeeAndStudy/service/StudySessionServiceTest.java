package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.domain.StudySession;
import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.PlaceRepository;
import com.Accesos.CoffeeAndStudy.repository.StudySessionRepository;
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
class StudySessionServiceTest {

    @Mock
    private StudySessionRepository studySessionRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudySessionService studySessionService;

    @Test
    void shouldReturnAllStudySessions() {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        StudySession session = new StudySession();
        session.setId(1L);
        session.setSessionDate(LocalDate.of(2025, 10, 6));
        session.setDurationMinutes(120);
        session.setProductivityLevel(4);
        session.setConsumedSomething(true);
        session.setTotalSpent(4.5f);
        session.setReservedTable(false);
        session.setNotes("Buena sesión de estudio");
        session.setPlace(place);
        session.setUser(user);

        when(studySessionRepository.findAll()).thenReturn(List.of(session));

        List<StudySession> result = studySessionService.filterStudySessions(null, null, null);

        assertEquals(1, result.size());
        assertEquals(120, result.get(0).getDurationMinutes());
    }

    @Test
    void shouldReturnStudySessionById() {
        StudySession session = new StudySession();
        session.setId(1L);
        session.setNotes("Buena sesión");

        when(studySessionRepository.findById(1L)).thenReturn(Optional.of(session));

        StudySession result = studySessionService.getStudySessionById(1L);

        assertEquals("Buena sesión", result.getNotes());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenStudySessionNotFound() {
        when(studySessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studySessionService.getStudySessionById(99L));
    }

    @Test
    void shouldSaveStudySession() {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        StudySession session = new StudySession();
        session.setSessionDate(LocalDate.of(2025, 10, 6));
        session.setDurationMinutes(120);
        session.setProductivityLevel(4);
        session.setConsumedSomething(true);
        session.setTotalSpent(4.5f);
        session.setReservedTable(false);
        session.setNotes("Buena sesión de estudio");
        session.setPlace(place);
        session.setUser(user);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studySessionRepository.save(any(StudySession.class))).thenReturn(session);

        StudySession result = studySessionService.saveStudySession(session);

        assertEquals(120, result.getDurationMinutes());
        verify(studySessionRepository, times(1)).save(any(StudySession.class));
    }

    @Test
    void shouldUpdateStudySession() {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        StudySession existingSession = new StudySession();
        existingSession.setId(1L);
        existingSession.setNotes("Antigua");

        StudySession updatedSession = new StudySession();
        updatedSession.setSessionDate(LocalDate.of(2025, 10, 7));
        updatedSession.setDurationMinutes(150);
        updatedSession.setProductivityLevel(5);
        updatedSession.setConsumedSomething(true);
        updatedSession.setTotalSpent(5.0f);
        updatedSession.setReservedTable(true);
        updatedSession.setNotes("Nueva sesión");
        updatedSession.setPlace(place);
        updatedSession.setUser(user);

        when(studySessionRepository.findById(1L)).thenReturn(Optional.of(existingSession));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studySessionRepository.save(any(StudySession.class))).thenReturn(existingSession);

        StudySession result = studySessionService.updateStudySession(1L, updatedSession);

        assertEquals("Nueva sesión", result.getNotes());
        assertEquals(150, result.getDurationMinutes());
    }

    @Test
    void shouldDeleteStudySession() {
        StudySession session = new StudySession();
        session.setId(1L);

        when(studySessionRepository.findById(1L)).thenReturn(Optional.of(session));

        studySessionService.deleteStudySession(1L);

        verify(studySessionRepository, times(1)).delete(session);
    }
}