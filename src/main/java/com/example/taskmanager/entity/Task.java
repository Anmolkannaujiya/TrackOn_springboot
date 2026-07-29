package com.example.taskmanager.entity;

import com.example.taskmanager.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Task {

    //primary key using ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @PrePersist
    public void onCreate(){
        //function that tells on creation that time
        //execute this before hibernate inset it into database
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        //only updated, createdAt is immutable
        updatedAt = LocalDateTime.now();
    }

}
