package com.example.service.impl;

import com.example.doamin.dao.Task;
import com.example.factory.TaskFactory;
import com.example.repository.TaskRepository;
import com.example.service.TaskService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger LOG = Logger.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final TaskFactory taskFactory;

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.listAll();
        LOG.infof("Retrieved %d tasks from the database", tasks.size());
        return tasks;
    }

    @Override
    public Task getTaskById(Long id) {
        LOG.infof("Fetching task with ID: %d", id);
        return taskRepository.findByIdOptional(id)
                .orElseThrow(() -> {
            LOG.warnf("Task with ID %d not found", id);
            return new EntityNotFoundException("Task with ID " + id + " not found");
        });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        LOG.infof("Deleting task with ID: %d", id);
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        LOG.infof("Creating task with title: %s", task.getTitle());
        taskRepository.persist(task);
        return task;
    }

    @Override
    @Transactional
    public Task generateRandomTask() {
        Task defaultTask = taskFactory.createDefaultTask();
        LOG.infof("Generated random task with title: %s", defaultTask.getTitle());
        taskRepository.persist(defaultTask);
        return defaultTask;
    }

    @Override
    @Transactional
    public void deleteAllTasks() {
        LOG.info("Deleting all tasks");
        taskRepository.deleteAll();
    }
}
