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
    public ResponseEntity<Void> addMeal(
            @PathVariable Long userId,
            @Valid @RequestBody MealDTO dto) {

        Meal meal = new Meal();
        meal.setMealType(dto.name());
        meal.setProtein(dto.protein());
        meal.setCarbs(dto.carbs());
        meal.setFats(dto.fats());
        meal.setDate(dto.date());

        mealService.addMeal(userId, meal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDTO>getMeal(@PathVariable Long id){
        return ResponseEntity.ok(mealService.getMealById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteMeal(@PathVariable Long id){
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.getMealsForUser(userId));
    }




}

