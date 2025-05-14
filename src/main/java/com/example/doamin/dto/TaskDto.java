package com.example.doamin.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String status;
    private LocalDateTime createdAt;
}
