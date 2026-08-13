package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequestDTO;
import com.example.taskmanager.dto.TaskResponseDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO dto){
        //response type changed -> response entity
        //jackson is called behind the scene for requestbody for getter and setter
        //return taskService.createTask(dto);

        //Restfull return on task creation

        TaskResponseDTO createdTask = taskService.createTask(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTask);
    }

    @GetMapping
    public Page<TaskResponseDTO> getAllTasks(
            @RequestParam(required=false) TaskStatus status,
            Pageable pageable){

        //import org.springframework.web.bind.annotation.PathVariable;
        //will use this
        //we are using query param, filtering by status
        return taskService.getAllTasks(status,pageable);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id){

        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO dto){

        //sending the request after converting json to dto using jackson
        return taskService.updateTask(id,dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);

        return  ResponseEntity.noContent().build();
    }

}
