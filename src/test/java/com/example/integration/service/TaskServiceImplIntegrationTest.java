package com.example.integration.service;

import com.example.doamin.Status;
import com.example.doamin.dao.Task;
import com.example.repository.TaskRepository;
import com.example.service.TaskService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceImplIntegrationTest {

    @Inject
    TaskService taskService;

    @Inject
    TaskRepository taskRepository;

    @AfterEach
    @Transactional
    void clean() {
        taskRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateTask() {
        //given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setStatus(Status.OPEN);

        //when
        Task created = taskService.createTask(task);

        //then
        Task taskFromDb = taskRepository.findById(created.id);
        assertEquals(taskFromDb.getTitle(), created.getTitle());
        assertEquals(taskFromDb.getStatus(), created.getStatus());
    }

    @Test
    @Transactional
    void shouldGetTaskById() {
        //given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setStatus(Status.OPEN);

        Task createdTask = taskService.createTask(task);

        //when
        Task result = taskService.getTaskById(createdTask.id);

        //then
        assertNotNull(result);
        assertEquals(result.getTitle(), task.getTitle());
        assertEquals(result.getStatus(), task.getStatus());
    }


    @Test
    @Transactional
    void shouldGetAllTasks() {
        //given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setStatus(Status.OPEN);

        Task task1 = new Task();
        task1.setTitle("Test Task 1");
        task1.setStatus(Status.CLOSED);

        Task task2 = new Task();
        task2.setTitle("Test Task 2");
        task2.setStatus(Status.CLOSED);

        taskRepository.persist(List.of(task, task1, task2));

        //when
        List<Task> tasks = taskService.getAllTasks();

        //then
        assertEquals(3, tasks.size());
        List<String> titles = tasks.stream().map(Task::getTitle).toList();
        assertTrue(titles.contains("Test Task"));
        assertTrue(titles.contains("Test Task 1"));
        assertTrue(titles.contains("Test Task 2"));
        assertTrue(tasks.stream().anyMatch(t -> t.getStatus() == Status.OPEN));
        assertEquals(2, tasks.stream().filter(t -> t.getStatus() == Status.CLOSED).count());

    }

    @Test
    @Transactional
    void shouldDeleteById() {
        //given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setStatus(Status.OPEN);

        Task created = taskService.createTask(task);

        //when
        taskService.deleteById(created.id);

        //then
        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(created.id));
    }

    @Test
    @Transactional
    void shouldDeleteAllTasks() {
        //given
        Task testTask = Task.builder()
                .title("Test task")
                .status(Status.OPEN)
                .build();

        Task testTask1 = Task.builder()
                .title("Test task")
                .status(Status.OPEN)
                .build();

        Task testTask2 = Task.builder()
                .title("Test task")
                .status(Status.CLOSED)
                .build();

        taskRepository.persist(List.of(testTask, testTask1, testTask2));


        //when
        taskService.deleteAllTasks();

        //then
        List<Task> tasksResults = taskRepository.listAll();
        assertEquals(0, tasksResults.size());
    }

}