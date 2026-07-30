package com.example.taskmanager.DTO;

import com.example.taskmanager.enums.TaskStatus;

import java.time.LocalDateTime;

public class TaskResponseDTO {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
