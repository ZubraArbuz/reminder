package com.example.reminder.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminderHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReminderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_id")
    private Reminder reminder;

    @Column(name = "changedAt", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "changes", nullable = false, columnDefinition = "TEXT")
    private String changes;
}
