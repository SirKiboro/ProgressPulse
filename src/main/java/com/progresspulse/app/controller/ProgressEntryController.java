package com.progresspulse.app.controller;

import com.progresspulse.app.dto.ProgressEntryDTO;
import com.progresspulse.app.model.ProgressEntry;
import com.progresspulse.app.service.ProgressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/progress")
public class ProgressEntryController {

    private final ProgressService progressService;

    public ProgressEntryController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping
    public ResponseEntity<Void> addProgressEntry(
            @PathVariable Long userId,
            @Valid @RequestBody ProgressEntryDTO dto) {

        ProgressEntry entry = new ProgressEntry();
        entry.setWeight(dto.weightKg());
        entry.setBodyFatPercent(dto.bodyFatPercent());
        entry.setDate(dto.date());

        ProgressEntry created = progressService.addProgressEntry(userId, entry);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProgressEntry>> getProgressTimeline(@PathVariable Long userId) {
        return ResponseEntity.ok(progressService.getProgressTimeline(userId));
    }

    @GetMapping
    public ResponseEntity<List<ProgressEntryDTO>> getAllProgressEntries(@PathVariable Long userId){
        return ResponseEntity.ok(progressService.getAllProgressEntries(userId));
    }
}

