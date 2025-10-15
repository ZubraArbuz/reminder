package com.example.reminder.controller;

import com.example.reminder.entity.Reminder;
import com.example.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remiders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder) {
        return ResponseEntity.ok(reminderService.createReminder(reminder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable int id) {
        return reminderService.getReminderById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<Reminder> getAllReminders(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                          @RequestParam(defaultValue = "asc") String direction,
                                          @RequestParam(required = false) String status) {
        return reminderService.getReminders(status,page,size,sortBy,direction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable int id, @RequestBody Reminder reminder) {
        return ResponseEntity.ok(reminderService.updateReminder(id, reminder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable int id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }
}
