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
    public ResponseEntity<Void> addWorkout(
            @PathVariable Long userId,
            @Valid @RequestBody WorkoutDTO workoutDTO) {

        Workout workout = new Workout();
        workout.setExerciseType(workoutDTO.type());
        workout.setDurationMinutes(workoutDTO.durationMinutes());
        workout.setCaloriesBurned(workoutDTO.caloriesBurned());
        workout.setDate(workoutDTO.date());

        workoutService.addWorkout(userId,workout);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkout(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getWorkoutById(id));
    }

    @GetMapping
    public ResponseEntity<List<Workout>> getAllWorkouts(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutService.getUserWorkouts(userId));
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void>deleteWorkout(
            @PathVariable Long userId,
            @PathVariable Long workoutId){

        workoutService.deleteWorkout(userId, workoutId);
        return ResponseEntity.noContent().build();
    }
}
