package com.progresspulse.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String mealType; // Breakfast, Lunch, Dinner, Snack

    private double protein; // grams
    private double carbs;   // grams
    private double fat;     // grams

    private double calories;

    private String foodItems;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
