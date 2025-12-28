package com.progresspulse.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.progresspulse.app.exception.ResourceNotFoundException;
import com.progresspulse.app.model.User;
import com.progresspulse.app.model.Workout;
import com.progresspulse.app.repository.UserRepository;
import com.progresspulse.app.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    private static final Logger log =
            LoggerFactory.getLogger(WorkoutService.class);


    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    // Create a workout for a specific user
    public void addWorkout(Long userId, Workout workout) {
        log.info("Creating workout for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (workout.getDate() == null) {
            workout.setDate(LocalDate.now());
        }

        workout.setUser(user);
        workoutRepository.save(workout);
    }

    // Retrieve all workouts for a user
    public List<Workout> getUserWorkouts(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return workoutRepository.findByUserId(userId);
    }

    // Retrieve a workout by ID
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + id));
    }

    // Delete a workout, ensures the workout belongs to the given user
    public void deleteWorkout(Long userId, Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));

        if (!workout.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Workout does not belong to user");
        }

        workoutRepository.delete(workout);
    }
}
