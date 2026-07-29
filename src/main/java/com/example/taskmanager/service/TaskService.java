package com.example.taskmanager.service;

import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
//stereotype annotation
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
