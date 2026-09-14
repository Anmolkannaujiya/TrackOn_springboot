package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    //taskrepository is an interface, interfaces cannot be instantiated i.e created object

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    //find the task that has this user id
    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(Long userId,TaskStatus status,Pageable pageable);

    Optional<Task> findByIdAndUserId(Long taskId,Long userId);

}
