package com.progresspulse.app.controller;

import com.progresspulse.app.dto.ProgressEntryDTO;
import com.progresspulse.app.model.ProgressEntry;
import com.progresspulse.app.model.User;
import com.progresspulse.app.service.ProgressService;
import com.progresspulse.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progress")
public class ProgressEntryController {

    private final ProgressService progressService;
    private final UserService userService;

    public ProgressEntryController(ProgressService progressService, UserService userService) {
        this.progressService = progressService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createEntry(@Valid @RequestBody ProgressEntryDTO dto) {
        // Ensure the user exists
        User user = userService.getUserById(dto.userId());

        ProgressEntry entry = new ProgressEntry();
        entry.setDate(dto.date());
        entry.setWeight(dto.weight());
        entry.setBodyFatPercent(dto.bodyFatPercent());
        entry.setNotes(dto.notes());

        progressService.addProgressEntry(user.getId(), entry);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressEntryDTO> getEntry(@PathVariable Long id) {
        ProgressEntry entry = progressService.getProgressEntryById(id);
        ProgressEntryDTO dto = new ProgressEntryDTO(
                entry.getDate(),
                entry.getWeight(),
                entry.getBodyFatPercent(),
                entry.getNotes(),
                entry.getUser().getId()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProgressEntryDTO>> getAllEntriesForUser(@PathVariable Long userId) {
        List<ProgressEntry> entries = progressService.getProgressTimeline(userId);
        List<ProgressEntryDTO> dtos = entries.stream()
                .map(e -> new ProgressEntryDTO(
                        e.getDate(),
                        e.getWeight(),
                        e.getBodyFatPercent(),
                        e.getNotes(),
                        e.getUser().getId()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{userId}/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long userId, @PathVariable Long entryId) {
        progressService.deleteProgressEntry(userId, entryId);
        return ResponseEntity.noContent().build();
    }
}
