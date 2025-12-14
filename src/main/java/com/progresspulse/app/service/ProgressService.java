package com.progresspulse.app.service;

import com.progresspulse.app.model.ProgressEntry;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.ProgressEntryRepository;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {

    private final ProgressEntryRepository progressRepository;
    private final UserRepository userRepository;

    public ProgressService(ProgressEntryRepository progressRepository,
                           UserRepository userRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    public ProgressEntry addProgressEntry(Long userId, ProgressEntry entry) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate date = entry.getDate() == null ? LocalDate.now() : entry.getDate();

        if (progressRepository.existsByUserIdAndDate(userId, date)) {
            throw new RuntimeException("Progress entry already exists for this date");
        }

        entry.setUser(user);
        entry.setDate(date);

        return progressRepository.save(entry);
    }

    public List<ProgressEntry> getProgressTimeline(Long userId) {
        return progressRepository.findByUserIdOrderByDateAsc(userId);
    }
}
