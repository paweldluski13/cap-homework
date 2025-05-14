package com.example.mapper;


import com.example.doamin.Status;
import com.example.doamin.dao.Task;
import com.example.doamin.dto.TaskDto;
import com.example.exception.WrongStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }


    @Test
    void shouldMapToDto() {
        //given
        Task task = new Task();
        task.id = 1L;
        task.setTitle("Test Task");
        task.setStatus(Status.OPEN);
        task.setCreatedAt(LocalDateTime.now());

        //when
        TaskDto dto = taskMapper.toDto(task);

        //then
        assertNotNull(dto);
        assertEquals(task.id, dto.getId());
        assertEquals("Test Task", dto.getTitle());
        assertEquals("OPEN", dto.getStatus());
        assertEquals(task.getCreatedAt(), dto.getCreatedAt());
    }


    @Test
    void shouldMapNullDaoToNull() {
        assertNull(taskMapper.toDto(null));
    }


    @Test
    void shouldMapToDtoList() {
        //given
        Task task1 = new Task();
        task1.id = 1L;
        task1.setTitle("Task 1");
        task1.setStatus(Status.OPEN);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.id = 2L;
        task2.setTitle("Task 2");
        task2.setStatus(Status.CLOSED);
        task2.setCreatedAt(LocalDateTime.now());

        //when
        List<TaskDto> dtoList = taskMapper.toDtoList(List.of(task1, task2));

        //then
        assertEquals(2, dtoList.size());
        assertEquals("Task 1", dtoList.get(0).getTitle());
        assertEquals("OPEN", dtoList.get(0).getStatus());
        assertEquals("Task 2", dtoList.get(1).getTitle());
        assertEquals("CLOSED", dtoList.get(1).getStatus());
    }

    @Test
    void shouldMapToDao() {
        //given
        TaskDto dto = new TaskDto();
        dto.setTitle("Mapped Task");
        dto.setStatus("OPEN");
        dto.setCreatedAt(LocalDateTime.now());

        //when
        Task task = taskMapper.toDao(dto);

        //then
        assertNotNull(task);
        assertEquals("Mapped Task", task.getTitle());
        assertEquals(Status.OPEN, task.getStatus());
        assertEquals(dto.getCreatedAt(), task.getCreatedAt());
    }


    @Test
    void shouldMapNullDtoToNull() {
        assertNull(taskMapper.toDao(null));
    }


    @Test
    void shouldThrowsWrongStatusException() {
        //given
        TaskDto dto = new TaskDto();
        dto.setStatus("INVALID_STATUS");

        //when + then
        assertThrows(WrongStatusException.class, () -> taskMapper.toDao(dto));
    }
}