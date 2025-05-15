package com.example.integration.resource;

import com.example.doamin.dto.TaskDto;
import com.example.repository.TaskRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class TaskResourceIntegrationTest {


    @Inject
    TaskRepository taskRepository;

    @BeforeEach
    @Transactional
    void clean() {
        taskRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateAndGetTaskById() {
        LocalDateTime now = LocalDateTime.now();
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Test Task");
        taskDto.setStatus("OPEN");
        taskDto.setCreatedAt(now);

        Long taskId = given()
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .post("/api/v1/tasks")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", equalTo("Test Task"))
                .body("createdAt", startsWith(now.truncatedTo(ChronoUnit.SECONDS).toString()))
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .when()
                .get("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("id", equalTo(taskId.intValue()))
                .body("title", equalTo("Test Task"))
                .body("status", equalTo("OPEN"));
    }


    @Test
    @Transactional
    void shouldDeleteTaskById() {
        //given
        LocalDateTime now = LocalDateTime.now();
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("To Delete");
        taskDto.setStatus("CLOSED");
        taskDto.setCreatedAt(now);

        Long taskId = given()
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .post("/api/v1/tasks")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        //when
        given()
                .when()
                .delete("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(204);

        //then
        assertTrue(taskRepository.listAll().isEmpty());
    }

    @Test
    @Transactional
    void shouldReturn404WhenTaskNotExist() {
        //given
        taskRepository.deleteAll();

        //when + then
        given()
                .when()
                .get("/api/v1/tasks/" + 1)
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    @Transactional
    void shouldReturn400WhenStatusIsWrong() {
        LocalDateTime now = LocalDateTime.now();
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("To Delete");
        taskDto.setStatus("WRONG");
        taskDto.setCreatedAt(now);

        given()
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .post("/api/v1/tasks")
                .then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    @Transactional
    void shouldGetAllTasks() {
        //given
        LocalDateTime now = LocalDateTime.now();
        TaskDto taskDto = TaskDto.builder()
                .title("To Delete")
                .status("CLOSED")
                .createdAt(now)
                .build();

        TaskDto taskDto1 = TaskDto.builder()
                .title("To Delete 1")
                .status("CLOSED")
                .createdAt(now)
                .build();

        TaskDto taskDto2 = TaskDto.builder()
                .title("To Delete 2")
                .status("OPEN")
                .createdAt(now)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .post("/api/v1/tasks");

        given()
                .contentType(ContentType.JSON)
                .body(taskDto1)
                .when()
                .post("/api/v1/tasks");

        given()
                .contentType(ContentType.JSON)
                .body(taskDto2)
                .when()
                .post("/api/v1/tasks");

        //when + then
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/tasks")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("title", hasItems("To Delete", "To Delete 1", "To Delete 2"))
                .body("status", hasItems("OPEN", "CLOSED", "CLOSED"));
    }

    @Test
    @Transactional
    void shouldDeleteAllTasks() {
        //given
        LocalDateTime now = LocalDateTime.now();
        TaskDto taskDto = TaskDto.builder()
                .title("To Delete")
                .status("CLOSED")
                .createdAt(now)
                .build();

        TaskDto taskDto1 = TaskDto.builder()
                .title("To Delete 1")
                .status("CLOSED")
                .createdAt(now)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .post("/api/v1/tasks");

        given()
                .contentType(ContentType.JSON)
                .body(taskDto1)
                .when()
                .post("/api/v1/tasks");

        //when
        given()
                .when()
                .delete("/api/v1/tasks")
                .then()
                .statusCode(204);

        //then
        assertTrue(taskRepository.listAll().isEmpty());
    }
}