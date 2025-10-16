package com.example.reminder.repository;

import com.example.reminder.entity.ReminderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderHistoryRepository extends JpaRepository<ReminderHistory,Integer> {
    List<ReminderHistory> findByReminderIdOrderByChangedAtDesc(int reminderId);
}
