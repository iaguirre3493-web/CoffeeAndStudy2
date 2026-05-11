package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ibone23");
        user.setEmail("ibone23@example.com");
        user.setPassword("password123");
        user.setAge(20);
        user.setPremium(true);
        user.setRegistrationDate(LocalDate.of(2025, 10, 1));
        user.setReputation(4.5f);

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.filterUsers(null, null, null);

        assertEquals(1, result.size());
        assertEquals("ibone23", result.get(0).getUsername());
    }

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ibone23");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("ibone23", result.getUsername());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setUsername("ibone23");
        user.setEmail("ibone23@example.com");
        user.setPassword("password123");
        user.setAge(20);
        user.setPremium(true);
        user.setRegistrationDate(LocalDate.of(2025, 10, 1));
        user.setReputation(4.5f);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals("ibone23", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");

        User updatedUser = new User();
        updatedUser.setUsername("newUser");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newpass123");
        updatedUser.setAge(21);
        updatedUser.setPremium(false);
        updatedUser.setRegistrationDate(LocalDate.of(2025, 10, 2));
        updatedUser.setReputation(4.8f);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("newUser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void shouldDeleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }
}