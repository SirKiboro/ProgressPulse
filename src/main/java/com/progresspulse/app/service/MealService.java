package com.progresspulse.app.service;

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        meal.setUser(user);
        meal.setDate(meal.getDate() == null ? LocalDate.now() : meal.getDate());

        return mealRepository.save(meal);
    }

    public List<Meal> getMealsForUser(Long userId) {
        return mealRepository.findByUserId(userId);
    }

    public List<Meal> getMealsForDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndDate(userId, date);
    }
}
