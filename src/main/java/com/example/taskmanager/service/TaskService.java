package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequestDTO;
import com.example.taskmanager.dto.TaskResponseDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
//stereotype annotation
public class TaskService {
    private final TaskRepository taskRepository;

    //mapper object injection
    private final TaskMapper taskMapper;

    //constructor
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    //POST
    //replacing Task task with the dto object in the parameter
    public TaskResponseDTO createTask(TaskRequestDTO dto){

        //sending to mapper before saving
        Task task = taskMapper.toEntity(dto);

        //after saving returning task entity converting to dto
        Task savedTask = taskRepository.save(task);

        //sending savedTask to mapper for responseDTO
        return taskMapper.toResponse(savedTask);
    }

    //GET
    public List<TaskResponseDTO> getAllTasks(){

        //hibernate using findall to return all task
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    //GET by ID
    public TaskResponseDTO getTaskById(Long id){

        //getting task by Id, using exception handling
//        Task task = taskRepository.findById(id)
//                .orElseThrow(()->new RuntimeException("Task not found"));

        //the above is general exception, below is more specific
        //user defined exception
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException(id));

        return taskMapper.toResponse(task);
    }
}
