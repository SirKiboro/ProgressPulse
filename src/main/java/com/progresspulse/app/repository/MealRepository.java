package com.progresspulse.app.repository;

import com.progresspulse.app.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUserId(Long userId);

    List<Meal> findByUserIdAndDate(Long userId, LocalDate date);
}
