package com.example.controller;

import com.example.doamin.Status;
import com.example.doamin.dao.Task;
import com.example.doamin.dto.TaskDto;
import com.example.exception.WrongStatusException;
import com.example.mapper.TaskMapper;
import com.example.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TaskResourceTest {

    @Mock
    TaskService taskService;

    @Mock
    TaskMapper taskMapper;

    @InjectMocks
    TaskResource taskResource;

    @Test
    void shouldReturnAllTasks() {
        //given
        List<Task> tasks = List.of(new Task("tests", Status.OPEN, LocalDateTime.now()),
                new Task("deployment", Status.OPEN, LocalDateTime.now()));
        List<TaskDto> taskDtos = List.of(new TaskDto(1L, "tests", "OPEN", LocalDateTime.now()),
                new TaskDto(2L, "deployment", "OPEN", LocalDateTime.now()));

        given(taskService.getAllTasks()).willReturn(tasks);
        given(taskMapper.toDtoList(tasks)).willReturn(taskDtos);

        //when
        List<TaskDto> result = taskResource.getAllTasks();

        //then
        assertEquals(2, result.size());
        verify(taskService).getAllTasks();
        verify(taskMapper).toDtoList(tasks);
    }

    @Test
    void shouldReturnTaskById() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("tests", Status.OPEN, now);
        task.id = 1L;
        TaskDto taskDto = new TaskDto(1L, "tests", "OPEN", now);

        given(taskService.getTaskById(1L)).willReturn(task);
        given(taskMapper.toDto(task)).willReturn(taskDto);

        //when
        TaskDto result = taskResource.getTaskById(1L);

        //then
        assertNotNull(result);
        verify(taskService).getTaskById(1L);
        verify(taskMapper).toDto(task);
    }


    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        //given
        given(taskService.getTaskById(1L)).willThrow(EntityNotFoundException.class);

        // when + then
        assertThrows(EntityNotFoundException.class, () -> taskResource.getTaskById(1L));
        verify(taskService).getTaskById(1L);
        verifyNoInteractions(taskMapper);
    }


    @Test
    void shouldDeleteTaskById() {
        //when
        Response response = taskResource.deleteTaskById(1L);

        //then
        assertEquals(204, response.getStatus());
        verify(taskService).deleteById(1L);
    }


    @Test
    void shouldSaveTask() {
        //given
        LocalDateTime now = LocalDateTime.now();
        TaskDto inputDto = TaskDto.builder()
                .title("Homework")
                .status("OPEN")
                .createdAt(now)
                .build();

        Task task = new Task("Homework", Status.OPEN, now);
        Task savedTask = new Task("Homework", Status.OPEN, now);
        savedTask.id = 1L;
        TaskDto outputDto = new TaskDto(1L, "Homework", "OPEN", now);

        given(taskMapper.toDao(inputDto)).willReturn(task);
        given(taskService.createTask(task)).willReturn(savedTask);
        given(taskMapper.toDto(savedTask)).willReturn(outputDto);

        //when
        TaskDto result = taskResource.saveTask(inputDto);

        //then
        assertNotNull(result);
        verify(taskMapper).toDao(inputDto);
        verify(taskService).createTask(task);
        verify(taskMapper).toDto(savedTask);
    }

    @Test
    void shouldThrowWrongStatusException() {
        //given
        LocalDateTime now = LocalDateTime.now();
        TaskDto inputDto = TaskDto.builder()
                .title("Homework")
                .status("postponed")
                .createdAt(now)
                .build();

        given(taskMapper.toDao(inputDto)).willThrow(WrongStatusException.class);


        //when + then
        assertThrows(WrongStatusException.class, () -> taskResource.saveTask(inputDto));
        verify(taskMapper).toDao(inputDto);
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDeleteAllTasks() {
        //when
        Response response = taskResource.deleteAllTasks();

        //then
        assertEquals(204, response.getStatus());
        verify(taskService).deleteAllTasks();
    }


}