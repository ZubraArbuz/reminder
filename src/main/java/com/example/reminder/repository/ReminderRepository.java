package com.example.reminder.repository;

import com.example.reminder.entity.Reminder;
import com.example.reminder.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder,Integer> {
    Page<Reminder> findAllByStatus(Status status, Pageable pageable);
}
