package com.progresspulse.app.service;

import com.progresspulse.app.dto.MealDTO;
import com.progresspulse.app.exception.ResourceNotFoundException;
import com.progresspulse.app.model.Meal;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.MealRepository;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    public List<Meal> getMealsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId);
        }
        return mealRepository.findByUserId(userId);
    }

    public List<Meal> getMealsForDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndDate(userId, date);
    }

    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meal not found with id: " + id));

        // Map entity to DTO
        return new MealDTO(
                meal.getUser().getName(),
                (int) meal.getProtein(),
                (int) meal.getCarbs(),
                (int) meal.getFats(),
                meal.getDate()
        );
    }

    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meal not found with id: " + id));
        if (!meal.getUser().getId().equals(id)) {
            throw new IllegalStateException("Meal does not belong to user");

        }
        mealRepository.delete(meal);
    }

}
