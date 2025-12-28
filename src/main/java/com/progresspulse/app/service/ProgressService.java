package com.progresspulse.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.progresspulse.app.exception.ResourceNotFoundException;
import com.progresspulse.app.model.ProgressEntry;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.ProgressEntryRepository;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {

    private final ProgressEntryRepository entryRepository;
    private final UserRepository userRepository;

    public ProgressService(ProgressEntryRepository entryRepository,
                           UserRepository userRepository) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
    }

    // Create a progress entry for a user
    public ProgressEntry addProgressEntry(Long userId, ProgressEntry entry) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (entry.getDate() == null) {
            entry.setDate(LocalDate.now());
        }

        // Prevent duplicate entries for the same date
        if (entryRepository.existsByUserIdAndDate(userId, entry.getDate())) {
            throw new IllegalStateException("Progress entry already exists for this date");
        }

        entry.setUser(user);
        return entryRepository.save(entry);
    }

    // Retrieve a single progress entry by ID
    public ProgressEntry getProgressEntryById(Long id) {
        return entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProgressEntry not found with ID: " + id));
    }

    // Retrieve all progress entries for a user, ordered by date
    public List<ProgressEntry> getProgressTimeline(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return entryRepository.findByUserIdOrderByDateAsc(userId);
    }

    // Delete a progress entry
    public void deleteProgressEntry(Long userId, Long entryId) {
        ProgressEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("ProgressEntry not found with ID: " + entryId));

        if (!entry.getUser().getId().equals(userId)) {
            throw new IllegalStateException("ProgressEntry does not belong to user");
        }

        entryRepository.delete(entry);
    }
}
