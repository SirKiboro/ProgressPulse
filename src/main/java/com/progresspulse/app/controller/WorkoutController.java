package com.progresspulse.app.controller;

import com.progresspulse.app.dto.WorkoutDTO;
import com.progresspulse.app.model.User;
import com.progresspulse.app.model.Workout;
import com.progresspulse.app.service.UserService;
import com.progresspulse.app.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createWorkout(@Valid @RequestBody WorkoutDTO dto) {
        User user = userService.getUserById(dto.userId());
        Workout workout = new Workout();
        workout.setDate(dto.date());
        workout.setExerciseType(dto.exerciseType());
        workout.setDurationMinutes(dto.durationMinutes());
        workout.setCaloriesBurned(dto.caloriesBurned());
        workout.setNotes(dto.notes());

        workoutService.addWorkout(user.getId(), workout);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkout(@PathVariable Long id) {
        Workout w = workoutService.getWorkoutById(id);
        WorkoutDTO dto = new WorkoutDTO(
                w.getDate(),
                w.getExerciseType(),
                w.getDurationMinutes(),
                w.getCaloriesBurned(),
                w.getNotes(),
                w.getUser().getId()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutDTO>> getAllWorkoutsForUser(@PathVariable Long userId) {
        List<Workout> workouts = workoutService.getUserWorkouts(userId);
        List<WorkoutDTO> dtos = workouts.stream()
                .map(w -> new WorkoutDTO(
                        w.getDate(),
                        w.getExerciseType(),
                        w.getDurationMinutes(),
                        w.getCaloriesBurned(),
                        w.getNotes(),
                        w.getUser().getId()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{userId}/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long userId, @PathVariable Long workoutId) {
        workoutService.deleteWorkout(userId, workoutId);
        return ResponseEntity.noContent().build();
    }
}
