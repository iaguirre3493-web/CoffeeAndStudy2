package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.GlobalExceptionHandler;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.service.UserService;
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
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userController, "userService", userService);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("ibone23");
        user.setEmail("ibone23@example.com");
        user.setPassword("password123");
        user.setAge(20);
        user.setPremium(true);
        user.setRegistrationDate(LocalDate.of(2025, 10, 1));
        user.setReputation(4.5f);

        when(userService.filterUsers(null, null, null)).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("ibone23"))
                .andExpect(jsonPath("$[0].age").value(20));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("ibone23");
        user.setEmail("ibone23@example.com");
        user.setPassword("password123");
        user.setAge(20);
        user.setPremium(true);
        user.setRegistrationDate(LocalDate.of(2025, 10, 1));
        user.setReputation(4.5f);

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ibone23"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.getUserById(99L))
                .thenThrow(new ResourceNotFoundException("User with id 99 not found"));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User();
        user.setUsername("ibone23");
        user.setEmail("ibone23@example.com");
        user.setPassword("password123");
        user.setAge(20);
        user.setPremium(true);
        user.setRegistrationDate(LocalDate.of(2025, 10, 1));
        user.setReputation(4.5f);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("ibone23");
        savedUser.setEmail("ibone23@example.com");
        savedUser.setPassword("password123");
        savedUser.setAge(20);
        savedUser.setPremium(true);
        savedUser.setRegistrationDate(LocalDate.of(2025, 10, 1));
        savedUser.setReputation(4.5f);

        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("ibone23"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPassword("newpass123");
        user.setAge(21);
        user.setPremium(false);
        user.setRegistrationDate(LocalDate.of(2025, 10, 2));
        user.setReputation(4.8f);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUser");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newpass123");
        updatedUser.setAge(21);
        updatedUser.setPremium(false);
        updatedUser.setRegistrationDate(LocalDate.of(2025, 10, 2));
        updatedUser.setReputation(4.8f);

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUser"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreatingInvalidUser() throws Exception {
        User invalidUser = new User();
        invalidUser.setUsername("");
        invalidUser.setEmail("correo-mal");
        invalidUser.setPassword("123");
        invalidUser.setAge(20);
        invalidUser.setPremium(true);
        invalidUser.setRegistrationDate(LocalDate.of(2025, 10, 1));
        invalidUser.setReputation(4.5f);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}