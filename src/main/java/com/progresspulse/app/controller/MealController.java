package com.progresspulse.app.controller;

import com.progresspulse.app.dto.MealDTO;
import com.progresspulse.app.model.Meal;
import com.progresspulse.app.model.User;
import com.progresspulse.app.service.MealService;
import com.progresspulse.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final UserService userService;

    public MealController(MealService mealService, UserService userService) {
        this.mealService = mealService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createMeal(@Valid @RequestBody MealDTO dto) {
        User user = userService.getUserById(dto.userId());
        Meal meal = new Meal();
        meal.setDate(dto.date());
        meal.setMealType(dto.mealType());
        meal.setProtein(dto.protein());
        meal.setCarbs(dto.carbs());
        meal.setFats(dto.fats());
        meal.setCalories(dto.calories());
        meal.setFoodItems(dto.foodItems());

        mealService.addMeal(user.getId(), meal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMeal(@PathVariable Long id) {
        MealDTO dto = mealService.getMealById(id); // Service already returns MealDTO
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealDTO>> getAllMealsForUser(@PathVariable Long userId) {
        List<MealDTO> dtos = mealService.getMealsForUser(userId)
                .stream()
                .map(meal -> new MealDTO(
                        meal.getDate(),
                        meal.getMealType(),
                        (BigDecimal) meal.getProtein(),
                        (BigDecimal) meal.getCarbs(),
                        (BigDecimal) meal.getFats(),
                        meal.getCalories(),
                        meal.getFoodItems(),
                        meal.getUser().getId()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{userId}/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long userId, @PathVariable Long mealId) {
        mealService.deleteMeal(userId, mealId);
        return ResponseEntity.noContent().build();
    }
}
