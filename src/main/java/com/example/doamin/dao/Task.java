package com.example.doamin.dao;

import com.example.doamin.Status;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "task")
public class Task extends PanacheEntity {
    private String title;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
}
