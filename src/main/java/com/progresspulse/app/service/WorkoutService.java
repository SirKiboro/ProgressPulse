package com.progresspulse.app.service;

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

    public WorkoutService(WorkoutRepository workoutRepository,
                          UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public Workout addWorkout(Long userId, Workout workout) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        workout.setUser(user);
        workout.setDate(workout.getDate() == null ? LocalDate.now() : workout.getDate());

        return workoutRepository.save(workout);
    }

    public List<Workout> getUserWorkouts(Long userId) {
        return workoutRepository.findByUserId(userId);
    }

    public List<Workout> getWeeklyWorkouts(Long userId,
                                           LocalDate start,
                                           LocalDate end) {
        return workoutRepository.findByUserIdAndDateBetween(userId, start, end);
    }
}
