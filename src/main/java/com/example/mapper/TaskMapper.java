package com.example.mapper;

import com.example.doamin.Status;
import com.example.doamin.dao.Task;
import com.example.doamin.dto.TaskDto;
import com.example.exception.WrongStatusException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TaskMapper {

    public TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        } else {
            TaskDto taskDto = new TaskDto();
            taskDto.setId(task.id);
            taskDto.setTitle(task.getTitle());
            taskDto.setStatus(statusToString(task.getStatus()));
            taskDto.setCreatedAt(task.getCreatedAt());
            return taskDto;
        }
    }

    public List<TaskDto> toDtoList(List<Task> tasks) {
        if (tasks == null) {
            return null;
        }

        List<TaskDto> list = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            list.add(toDto(task));
        }

        return list;
    }


    public Task toDao(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        } else {
            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setStatus(stringToStatus(taskDto.getStatus()));
            task.setCreatedAt(taskDto.getCreatedAt());
            return task;
        }
    }


    private String statusToString(Status status) {
        return status != null ? status.name() : null;
    }


    private Status stringToStatus(String status) {
        if (status == null) {
            return null;
        }

        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new WrongStatusException("Invalid status value: " + status);
        }
    }

}
