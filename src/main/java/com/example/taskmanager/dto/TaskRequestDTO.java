package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {

    //including validation on DTO attributes
    @NotBlank(message = "Title cannot be blank")
    @Size(min =3,max=100,message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min=5,max = 300, message = "Description must be between 5 and 300 characters")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
