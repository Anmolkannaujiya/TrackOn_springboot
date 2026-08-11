package com.example.taskmanager.mapper;


import com.example.taskmanager.dto.TaskResponseDTO;
import org.springframework.stereotype.Component;
import com.example.taskmanager.dto.TaskRequestDTO;
import com.example.taskmanager.entity.Task;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequestDTO dto){
        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        return task;
    }

    public TaskResponseDTO toResponse(Task task){
        TaskResponseDTO dto = new TaskResponseDTO();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        return dto;
    }

    public void updateEntity(Task task,TaskRequestDTO){
        
    }

}
