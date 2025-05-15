package com.example.factory;

import com.example.doamin.Status;
import com.example.doamin.dao.Task;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class TaskFactory {

    public Task createDefaultTask() {
        return Task.builder()
                .title("Default Task " + UUID.randomUUID())
                .status(Status.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
