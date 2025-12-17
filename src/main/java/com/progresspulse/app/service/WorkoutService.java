package com.progresspulse.app.service;

import com.progresspulse.app.dto.WorkoutDTO;
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

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public void addWorkout(Long userId, Workout workout) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        if (workout.getDate() == null) {
            workout.setDate(LocalDate.now());
        }

        workout.setUser(user);
        workoutRepository.save(workout);
    }

    public List<Workout> getUserWorkouts(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId);
        }
        return workoutRepository.findByUserId(userId);
    }

    public void deleteWorkout(Long userId, Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found with id: " + workoutId));

        if (!workout.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Workout does not belong to user");
        }

        workoutRepository.delete(workout);
    }

    public WorkoutDTO getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found with id: " + id));

        // Map entity to DTO
        return new WorkoutDTO(
                workout.getId(),
                workout.getExerciseType(),
                workout.getDurationMinutes(),
                workout.getCaloriesBurned(),
                workout.getDate()
        );
    }

}
