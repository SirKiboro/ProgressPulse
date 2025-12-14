package com.progresspulse.app.controller;

import com.progresspulse.app.dto.MealDTO;
import com.progresspulse.app.model.Meal;
import com.progresspulse.app.service.MealService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<Meal> addMeal(
            @PathVariable Long userId,
            @Valid @RequestBody MealDTO dto) {

        Meal meal = new Meal();
        meal.setMealType(dto.name());
        meal.setProtein(dto.protein());
        meal.setCarbs(dto.carbs());
        meal.setFats(dto.fats());
        meal.setDate(dto.date());

        Meal created = mealService.addMeal(userId, meal);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.getMealsForUser(userId));
    }
}

