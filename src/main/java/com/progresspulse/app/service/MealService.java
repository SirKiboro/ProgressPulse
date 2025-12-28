package com.progresspulse.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.progresspulse.app.dto.MealDTO;
import com.progresspulse.app.exception.ResourceNotFoundException;
import com.progresspulse.app.model.Meal;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.MealRepository;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealService(MealRepository mealRepository,
                       UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    // Adds a meal for a user, sets current date if not provided
    public Meal addMeal(Long userId, Meal meal) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));

        if (meal.getDate() == null) {
            meal.setDate(LocalDate.now());
        }

        meal.setUser(user);
        return mealRepository.save(meal);
    }

    // Retrieves all meals for a specific user
    public List<Meal> getMealsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return mealRepository.findByUserId(userId);
    }

    // Retrieves meals for a specific user and date
    public List<Meal> getMealsForDate(Long userId, LocalDate date) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return mealRepository.findByUserIdAndDate(userId, date);
    }

    // Retrieves a meal by its ID
    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meal not found with id: " + id));

        return new MealDTO(
                meal.getDate(),
                meal.getMealType(),
                (BigDecimal) meal.getProtein(),
                (BigDecimal) meal.getCarbs(),
                (BigDecimal) meal.getFats(),
                meal.getCalories(),
                meal.getFoodItems(),
                meal.getUser().getId()
        );
    }

    // Deletes a meal by ID
    public void deleteMeal(Long id, Long mealId) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with ID: " + id));

        mealRepository.delete(meal);
    }
}
