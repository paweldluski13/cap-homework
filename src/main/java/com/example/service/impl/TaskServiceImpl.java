package com.example.service.impl;

import com.example.doamin.dao.Task;
import com.example.repository.TaskRepository;
import com.example.service.TaskService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.listAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        taskRepository.persist(task);
        return task;
    }
}
