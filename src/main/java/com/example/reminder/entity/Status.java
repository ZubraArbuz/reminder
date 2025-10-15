package com.example.reminder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statuses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String name;

    @Override
    public String toString(){
        return name;
    }
}
