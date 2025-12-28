package com.progresspulse.app.repository;

import com.progresspulse.app.model.ProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProgressEntryRepository extends JpaRepository<ProgressEntry, Long> {

    List<ProgressEntry> findByUserId(Long userId);

    List<ProgressEntry> findByUserIdOrderByDateAsc(Long userId);

    boolean existsByUserIdAndDate(Long userId, LocalDate date);

}
