package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.domain.StudySession;
import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.GlobalExceptionHandler;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.service.StudySessionService;
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
class StudySessionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private StudySessionService studySessionService;

    @InjectMocks
    private StudySessionController studySessionController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(studySessionController, "studySessionService", studySessionService);

        mockMvc = MockMvcBuilders.standaloneSetup(studySessionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldReturnAllStudySessions() throws Exception {
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

        when(studySessionService.filterStudySessions(null, null, null)).thenReturn(List.of(session));

        mockMvc.perform(get("/study-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].durationMinutes").value(120))
                .andExpect(jsonPath("$[0].productivityLevel").value(4));
    }

    @Test
    void shouldReturnStudySessionById() throws Exception {
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

        when(studySessionService.getStudySessionById(1L)).thenReturn(session);

        mockMvc.perform(get("/study-sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.durationMinutes").value(120));
    }

    @Test
    void shouldReturn404WhenStudySessionNotFound() throws Exception {
        when(studySessionService.getStudySessionById(99L))
                .thenThrow(new ResourceNotFoundException("StudySession with id 99 not found"));

        mockMvc.perform(get("/study-sessions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateStudySession() throws Exception {
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

        StudySession savedSession = new StudySession();
        savedSession.setId(1L);
        savedSession.setSessionDate(LocalDate.of(2025, 10, 6));
        savedSession.setDurationMinutes(120);
        savedSession.setProductivityLevel(4);
        savedSession.setConsumedSomething(true);
        savedSession.setTotalSpent(4.5f);
        savedSession.setReservedTable(false);
        savedSession.setNotes("Buena sesión de estudio");
        savedSession.setPlace(place);
        savedSession.setUser(user);

        when(studySessionService.saveStudySession(any(StudySession.class))).thenReturn(savedSession);

        mockMvc.perform(post("/study-sessions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.durationMinutes").value(120));
    }

    @Test
    void shouldUpdateStudySession() throws Exception {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        StudySession session = new StudySession();
        session.setSessionDate(LocalDate.of(2025, 10, 7));
        session.setDurationMinutes(150);
        session.setProductivityLevel(5);
        session.setConsumedSomething(true);
        session.setTotalSpent(5.0f);
        session.setReservedTable(true);
        session.setNotes("Nueva sesión");
        session.setPlace(place);
        session.setUser(user);

        StudySession updatedSession = new StudySession();
        updatedSession.setId(1L);
        updatedSession.setSessionDate(LocalDate.of(2025, 10, 7));
        updatedSession.setDurationMinutes(150);
        updatedSession.setProductivityLevel(5);
        updatedSession.setConsumedSomething(true);
        updatedSession.setTotalSpent(5.0f);
        updatedSession.setReservedTable(true);
        updatedSession.setNotes("Nueva sesión");
        updatedSession.setPlace(place);
        updatedSession.setUser(user);

        when(studySessionService.updateStudySession(eq(1L), any(StudySession.class))).thenReturn(updatedSession);

        mockMvc.perform(put("/study-sessions/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Nueva sesión"));
    }

    @Test
    void shouldDeleteStudySession() throws Exception {
        doNothing().when(studySessionService).deleteStudySession(1L);

        mockMvc.perform(delete("/study-sessions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreatingInvalidStudySession() throws Exception {
        Place place = new Place();
        place.setId(1L);

        User user = new User();
        user.setId(1L);

        StudySession invalidSession = new StudySession();
        invalidSession.setSessionDate(LocalDate.of(2025, 10, 6));
        invalidSession.setDurationMinutes(0);
        invalidSession.setProductivityLevel(8);
        invalidSession.setConsumedSomething(true);
        invalidSession.setTotalSpent(4.5f);
        invalidSession.setReservedTable(false);
        invalidSession.setNotes("Buena sesión de estudio");
        invalidSession.setPlace(place);
        invalidSession.setUser(user);

        mockMvc.perform(post("/study-sessions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSession)))
                .andExpect(status().isBadRequest());
    }
}