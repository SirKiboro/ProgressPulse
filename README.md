# ProgressPulse

[![Java](https://img.shields.io/badge/Java-17+-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](LICENSE)

**ProgressPulse** is a web-based fitness and progress tracking application built with Spring Boot.  
It allows users to log workouts, meals, and body metrics to monitor fitness progress over time.

---

## Features

- User Management (CRUD operations)
- Workout Logging (exercise type, duration, calories)
- Meal Tracking (macronutrients: protein, carbs, fat)
- Progress Entries (weight, body fat percentage)
- Time-series analytics for fitness tracking
- RESTful API for integration with frontend or third-party apps
- Data validation and integrity enforcement

---

## Tech Stack

- **Language:** Java 17+
- **Framework:** Spring Boot 3+
- **Persistence:** Spring Data JPA
- **Database:** H2 (development) / PostgreSQL (production-ready)
- **Libraries:** Lombok, Validation API
- **Build Tool:** Maven

---

## Project Structure

com.progresspulse.app
├── controller # REST controllers
├── service # Business logic
├── repository # Database access
├── model # Entities
├── dto # Data transfer objects
├── config # Configuration files
├── exception # Custom exceptions
└── util # Helper utilities
