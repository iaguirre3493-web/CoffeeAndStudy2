package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.User;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> filterUsers(String username, Boolean premium, Integer age) {
        logger.info("Filtering users with username={}, premium={}, age={}", username, premium, age);

        if (username != null && premium != null && age != null) {
            return userRepository.findByUsernameContainingIgnoreCaseAndPremiumAndAge(username, premium, age);
        }
        if (username != null) {
            return userRepository.findByUsernameContainingIgnoreCase(username);
        }
        if (premium != null) {
            return userRepository.findByPremium(premium);
        }
        if (age != null) {
            return userRepository.findByAge(age);
        }
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        logger.info("Getting user by id={}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found", id);
                    return new ResourceNotFoundException("User with id " + id + " not found");
                });
    }

    public User saveUser(User user) {
        logger.info("Saving new user with username={}", user.getUsername());
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        logger.info("Updating user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found for update", id);
                    return new ResourceNotFoundException("User with id " + id + " not found");
                });

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setAge(updatedUser.getAge());
        user.setPremium(updatedUser.getPremium());
        user.setRegistrationDate(updatedUser.getRegistrationDate());
        user.setReputation(updatedUser.getReputation());

        logger.info("User with id {} updated successfully", id);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found for delete", id);
                    return new ResourceNotFoundException("User with id " + id + " not found");
                });

        userRepository.delete(user);
        logger.info("User with id {} deleted successfully", id);
    }
}