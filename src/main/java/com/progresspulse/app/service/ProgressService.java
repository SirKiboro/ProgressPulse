package com.progresspulse.app.service;

import com.progresspulse.app.dto.ProgressEntryDTO;
import com.progresspulse.app.exception.ResourceNotFoundException;
import com.progresspulse.app.model.ProgressEntry;
import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.ProgressEntryRepository;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));

        LocalDate date = entry.getDate();
        if (entry.getDate() == null) {
            entry.setDate(LocalDate.now());
        }

        if (progressRepository.existsByUserIdAndDate(userId, date)) {
            throw new ResourceNotFoundException("Progress entry already exists for this date");
        }


        entry.setUser(user);
        return progressRepository.save(entry);
    }

    public List<ProgressEntry> getProgressTimeline(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId);
        }
        return progressRepository.findByUserIdOrderByDateAsc(userId);
    }


    public List<ProgressEntryDTO> getAllProgressEntries(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId);

        }
        return progressRepository.findByUserId(userId).stream()
                .map(entry -> new ProgressEntryDTO(
                        entry.getId(),
                        entry.getUser().getId(),
                        entry.getWeight(),
                        entry.getBodyFatPercent(),
                        entry.getDate()
                        
                ))
                .collect(Collectors.toList());
    }
}
