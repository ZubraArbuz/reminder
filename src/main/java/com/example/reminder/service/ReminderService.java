package com.example.reminder.service;

import com.example.reminder.entity.Reminder;
import com.example.reminder.entity.ReminderHistory;
import com.example.reminder.entity.Status;
import com.example.reminder.repository.ReminderHistoryRepository;
import com.example.reminder.repository.ReminderRepository;
import com.example.reminder.repository.StatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderHistoryRepository historyRepository;
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

        if (sortBy.equals("status")) {
            List<Reminder> all = reminderRepository.findAll();
            Comparator<Reminder> comparator = Comparator.comparingInt(r -> switch (r.getStatus().getName()) {
                case "Новый" -> 4;
                case "Запланирован" -> 3;
                case "Просрочен" -> 2;
                case "Исполнен" -> 1;
                default -> 0;
            });

            if (direction.equals("desc")) comparator = comparator.reversed();

            all.sort(comparator);
            int start = page * size;
            int end = Math.min(start + size, all.size());
            return new PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
        }

        return reminderRepository.findAll(pageable);
    }

    @Transactional
    public Reminder updateReminder(int id, Reminder updReminder) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Напоминание не найдено"));

        StringBuilder changes = new StringBuilder();

        if (!Objects.equals(reminder.getShortDescription(), updReminder.getShortDescription())) {
            changes.append("Изменено краткое описание: ")
                    .append(reminder.getShortDescription()).append(" → ")
                    .append(updReminder.getShortDescription()).append("\n");
            reminder.setShortDescription(updReminder.getShortDescription());
        }

        if (!Objects.equals(reminder.getFullDescription(), updReminder.getFullDescription())) {
            changes.append("Изменено полное описание.\n");
            reminder.setFullDescription(updReminder.getFullDescription());
        }

        if (!Objects.equals(reminder.getDueDate(), updReminder.getDueDate())) {
            changes.append("Изменена дата выполнения: ")
                    .append(reminder.getDueDate()).append(" → ")
                    .append(updReminder.getDueDate()).append("\n");
            reminder.setDueDate(updReminder.getDueDate());
        }

        if (updReminder.getStatus() != null && !Objects.equals(reminder.getStatus(), updReminder.getStatus())) {
            changes.append("Изменён статус: ")
                    .append(reminder.getStatus().getName()).append(" → ")
                    .append(updReminder.getStatus().getName()).append("\n");
            reminder.setStatus(updReminder.getStatus());
        }

        Reminder saved = reminderRepository.save(reminder);

        if (changes.length() > 0) {
            historyRepository.save(ReminderHistory.builder()
                    .reminder(saved)
                    .changedAt(LocalDateTime.now())
                    .changes(changes.toString())
                    .build());
        }

        return saved;
    }

    public void deleteReminder(int id) {
        reminderRepository.deleteById(id);
    }

    public List<ReminderHistory> getHistoryByReminder(int reminderId) {
        return historyRepository.findByReminderIdOrderByChangedAtDesc(reminderId);
    }
}
