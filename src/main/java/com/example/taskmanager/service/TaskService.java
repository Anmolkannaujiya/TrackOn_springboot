package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequestDTO;
import com.example.taskmanager.dto.TaskResponseDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
//import com.example.taskmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//import java.util.List;

@Service
//stereotype annotation
public class TaskService {
    private final TaskRepository taskRepository;

    //mapper object injection
    private final TaskMapper taskMapper;

    //mapping users using currentUser to tasks
    private final CurrentUserService currentUserService;

    //constructor
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper,CurrentUserService currentUserService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.currentUserService = currentUserService;
    }

    //POST
    //replacing Task task with the dto object in the parameter
    public TaskResponseDTO createTask(TaskRequestDTO dto){

        //injecting current user
        User currentUser = currentUserService.getCurrentUser();

        //sending to mapper before saving
        Task task = taskMapper.toEntity(dto);

        //setting the user
        task.setUser(currentUser);

        //after saving returning task entity converting to dto
        Task savedTask = taskRepository.save(task);

        //sending savedTask to mapper for responseDTO
        return taskMapper.toResponse(savedTask);
    }

    //GET
    /*
    public List<TaskResponseDTO> getAllTasks(){

        //hibernate using findall to return all task
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }
    */

    //GET all tasks but using pagination
    public Page<TaskResponseDTO> getAllTasks(
            TaskStatus status,
            Pageable pageable){

        User currentUser = currentUserService.getCurrentUser();

        Page<Task> taskPage;

        //handling both by status and by findall
        if(status != null){
            taskPage = taskRepository.findByUserIdAndStatus(
                    currentUser.getId(),
                    status,
                    pageable
            );
        }
        else{
            taskPage = taskRepository.findByUserId(
                    currentUser.getId(),
                    pageable
            );
        }

        return taskPage.map(taskMapper::toResponse);
    }

    //GET by ID
    public TaskResponseDTO getTaskById(Long id){

        //getting task by Id, using exception handling
//        Task task = taskRepository.findById(id)
//                .orElseThrow(()->new RuntimeException("Task not found"));

        //the above is general exception, below is more specific
        //user defined exception

        //mapping user-> current user
        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository.findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()->new TaskNotFoundException(id));

        return taskMapper.toResponse(task);
    }

    //put by id
    public TaskResponseDTO updateTask(Long id,TaskRequestDTO dto){

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()-> new TaskNotFoundException(id));

        //saves task current object with dto values
        taskMapper.updateEntity(task,dto);

        Task updatedtask = taskRepository.save(task);

        return taskMapper.toResponse(updatedtask);
    }

    //delete by id
    public void deleteTask(Long id){

        User currentUser = currentUserService.getCurrentUser();

        //finding if the task exist or not
        Task task = taskRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(()->new TaskNotFoundException(id));

        taskRepository.delete(task);
    }

}
