package com.example.reminder.config;

import com.example.reminder.entity.Status;
import com.example.reminder.repository.StatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final StatusRepository statusRepository;

    @PostConstruct
    public void init() {
        List<String> statuses = List.of("Новый","Запланирован", "Просрочен", "Исполнен");

        for (String status : statuses) {
            statusRepository.findByName(status).ifPresentOrElse(
                    status1 -> {},
                    () -> statusRepository.save(Status.builder().name(status).build())
            );
        }
    }
}
