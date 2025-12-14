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

public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Column(nullable = false)
    private String exerciseType;

    private int durationMinutes;

    private double caloriesBurned;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
