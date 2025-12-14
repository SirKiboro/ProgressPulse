package com.progresspulse.app.controller;

import com.progresspulse.app.dto.WorkoutDTO;
import com.progresspulse.app.model.Workout;
import com.progresspulse.app.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<Workout> addWorkout(
            @PathVariable Long userId,
            @Valid @RequestBody WorkoutDTO dto) {

        Workout workout = new Workout();
        workout.setExerciseType(dto.type());
        workout.setDurationMinutes(dto.durationMinutes());
        workout.setCaloriesBurned(dto.caloriesBurned());
        workout.setDate(dto.date());

        Workout created = workoutService.addWorkout(userId, workout);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Workout>> getAllWorkouts(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutService.getUserWorkouts(userId));
    }
}
