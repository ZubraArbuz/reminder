package com.example.reminder.service;

import com.example.reminder.entity.Reminder;
import com.example.reminder.entity.Status;
import com.example.reminder.repository.ReminderRepository;
import com.example.reminder.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final StatusRepository statusRepository;

    public Reminder createReminder(Reminder reminder) {
        if(reminder.getStatus() == null){
            Status newStatus = statusRepository.findByName("Новый").orElseThrow(() -> new RuntimeException("Статус 'Новый' не найден"));
            reminder.setStatus(newStatus);
        }
        reminder.setCreatedAt(java.time.LocalDateTime.now());
        return reminderRepository.save(reminder);
    }

    public Optional<Reminder> getReminderById(int id) {
        return reminderRepository.findById(id);
    }

    public Page<Reminder> getReminders(String statusName, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        if (statusName != null && !statusName.isEmpty()) {
            Status status = statusRepository.findByName(statusName).orElseThrow(() -> new RuntimeException("Статус не найден"));
            return reminderRepository.findAllByStatus(status, pageable);
        }

        return reminderRepository.findAll(pageable);
    }

    public Reminder updateReminder(int id, Reminder updReminder) {
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new RuntimeException("Напоминание не найдено"));
        reminder.setShortDescription(updReminder.getShortDescription());
        reminder.setFullDescription(updReminder.getFullDescription());
        reminder.setDueDate(updReminder.getDueDate());
        reminder.setStatus(updReminder.getStatus());
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(int id) {
        reminderRepository.deleteById(id);
    }
}
