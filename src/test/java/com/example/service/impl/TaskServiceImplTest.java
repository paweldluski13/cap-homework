package com.example.service.impl;

import com.example.doamin.Status;
import com.example.doamin.dao.Task;
import com.example.factory.TaskFactory;
import com.example.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskFactory taskFactory;

    @InjectMocks
    TaskServiceImpl taskServiceImpl;


    @Test
    void shouldGetAllTasks() {
        //given
        List<Task> tasks = List.of(new Task("tests", Status.OPEN, LocalDateTime.now()),
                new Task("deployment", Status.OPEN, LocalDateTime.now()));
        given(taskRepository.listAll()).willReturn(tasks);

        //when
        List<Task> result = taskServiceImpl.getAllTasks();

        //then
        assertEquals(2, result.size());
        verify(taskRepository).listAll();
    }

    @Test
    void shouldGetEmptyList() {
        //given
        given(taskRepository.listAll()).willReturn(Collections.emptyList());

        //when
        List<Task> result = taskServiceImpl.getAllTasks();

        //then
        assertEquals(0, result.size());
        verify(taskRepository).listAll();
    }

    @Test
    void shouldGetTaskById() {
        //given
        long id = 1L;
        Task task = new Task("tests", Status.OPEN, LocalDateTime.now());
        task.id = id;
        given(taskRepository.findByIdOptional(id)).willReturn(Optional.of(task));

        //when
        Task result = taskServiceImpl.getTaskById(1L);

        //then
        assertEquals(task, result);
        verify(taskRepository).findByIdOptional(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException() {
        //given
        given(taskRepository.findByIdOptional(1L)).willReturn(Optional.empty());

        //when + then
        assertThrows(EntityNotFoundException.class, () -> taskServiceImpl.getTaskById(1L));
    }

    @Test
    void shouldDeleteTaskById() {
        //given
        long id = 1L;

        //when
        taskServiceImpl.deleteById(id);

        //then
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldCreateTask() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("tests", Status.OPEN, now);

        //when
        Task result = taskServiceImpl.createTask(task);

        //then
        verify(taskRepository).persist(task);
        assertEquals("tests", result.getTitle());
        assertEquals(Status.OPEN, result.getStatus());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    void shouldReturnGeneratedTask() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Task randomTask = Task.builder()
                .title("Random Task")
                .status(Status.OPEN)
                .createdAt(now)
                .build();
        given(taskFactory.createDefaultTask()).willReturn(randomTask);

        //when
        Task result = taskServiceImpl.generateRandomTask();

        //then
        verify(taskRepository).persist(result);
        assertEquals("Random Task", result.getTitle());
        assertEquals(Status.OPEN, result.getStatus());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    void shouldRemoveAllTasks() {
        //when
        taskServiceImpl.deleteAllTasks();

        //then
        verify(taskRepository,times(1)).deleteAll();
    }
}