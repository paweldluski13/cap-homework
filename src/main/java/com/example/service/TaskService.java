package com.example.service;

import com.example.doamin.dao.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task getTaskById(Long id);

    void deleteById(Long id);

    Task createTask(Task task);

    Task generateRandomTask();

    void deleteAllTasks();
}
