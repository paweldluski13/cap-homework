package com.example.integration.resource;

import com.example.doamin.dto.TaskDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class TaskResourceIntegrationTest {

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

        given()
                .when()
                .delete("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(204);
    }

    @Test
    @Transactional
    void shouldReturn404WhenTaskNotExist() {
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

        taskId = taskId + 10;

        given()
                .when()
                .get("/api/v1/tasks/" + taskId)
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

}