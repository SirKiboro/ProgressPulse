# ProgressPulse

[![Java](https://img.shields.io/badge/Java-17+-blue)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)



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

- `com.progresspulse.app.controller` – REST controllers  
- `com.progresspulse.app.service` – Business logic  
- `com.progresspulse.app.repository` – Database access  
- `com.progresspulse.app.model` – Entities  
- `com.progresspulse.app.dto` – Data transfer objects  
- `com.progresspulse.app.config` – Configuration files  
- `com.progresspulse.app.exception` – Custom exceptions  
- `com.progresspulse.app.util` – Helper utilities  

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
