package com.progresspulse.app.service;

import com.progresspulse.app.model.Gender;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    User sampleUser = new User();

    @BeforeEach
    void setUp(){
        //initialize user

        sampleUser.setName("John Doe");
        sampleUser.setEmail("john@example.com");
        sampleUser.setGender(Gender.valueOf("MALE"));
        sampleUser.setHeightCm(BigDecimal.valueOf(172));
        sampleUser.setDateOfBirth(LocalDate.of(2000,12,1));
    }

        //Tests...

    @Test
    void createUser_Successfully(){

       when(userRepository.save(any(User.class))).thenReturn(sampleUser);

       User created = userService.createUser(sampleUser);

       assertNotNull(created);
       assertEquals("john@example.com", created.getEmail());
       verify(userRepository, times(1)).save(any(User.class));


    }

    @Test
    void createUser_shouldThrowException_whenEmailAlreadyExists() {
        // 1. Arrange
        String email = "john@example.com";
        User duplicateUser = new User();
        duplicateUser.setEmail(email);

        // Tell the mock: "Yes, this email already exists"
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // 2. Act & Assert
        // We expect an IllegalArgumentException because of your "throw new..." line
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(duplicateUser);
        });

        // Verify the error message matches your code
        assertEquals("Email already in use", exception.getMessage());

        // IMPORTANT: Verify that save() was NEVER called
        // This proves the "if" statement stopped the process
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        Long id = 99L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(id);
        });
    }
}
