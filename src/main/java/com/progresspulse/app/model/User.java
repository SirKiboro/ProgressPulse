package com.progresspulse.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Age is derived, not stored
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal heightCm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, length = 255)
    private String email;

    // --- Relationships ---

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Workout> workouts = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<ProgressEntry> progressEntries = new ArrayList<>();

    // --- Audit fields ---

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    // --- Relationship helpers ---

    public void addWorkout(Workout workout) {
        workouts.add(workout);
        workout.setUser(this);
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
        workout.setUser(null);
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
        meal.setUser(this);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
        meal.setUser(null);
    }

    public void addProgressEntry(ProgressEntry entry) {
        progressEntries.add(entry);
        entry.setUser(this);
    }

    public void removeProgressEntry(ProgressEntry entry) {
        progressEntries.remove(entry);
        entry.setUser(null);
    }
}