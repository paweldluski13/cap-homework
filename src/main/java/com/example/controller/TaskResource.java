package com.example.controller;

import com.example.doamin.dto.TaskDto;
import com.example.mapper.TaskMapper;
import com.example.service.TaskService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/v1/tasks")
public class TaskResource {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GET
    public List<TaskDto> getAllTasks() {
        return taskMapper.toDtoList(taskService.getAllTasks());
    }

    @GET
    @Path("/{id}")
    public TaskDto getTaskById(@PathParam("id") Long id) {
        return taskMapper.toDto(taskService.getTaskById(id));
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTaskById(@PathParam("id") Long id) {
        taskService.deleteById(id);
        return Response.noContent().build();
    }

    @POST
    public TaskDto saveTask(TaskDto taskDto) {
        return taskMapper.toDto(taskService.createTask(taskMapper.toDao(taskDto)));
    }

    @GET
    @Path("/generateRandomTask")
    public TaskDto generateRandomTask() {
        return taskMapper.toDto(taskService.generateRandomTask());
    }

    @DELETE
    public Response deleteAllTasks() {
        taskService.deleteAllTasks();
        return Response.noContent().build();
    }
}
