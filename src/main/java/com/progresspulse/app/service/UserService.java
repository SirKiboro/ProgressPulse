package com.progresspulse.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.progresspulse.app.exception.ResourceNotFoundException;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Creates user with duplicate email check
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        return userRepository.save(user);
    }

    // Retrieves user by ID or throws ResourceNotFoundException
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    // Retrieves all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Deletes user by ID after checking existence
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
