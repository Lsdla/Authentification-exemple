package com.lyessou.authentification.controller;

import com.lyessou.authentification.entity.Task;
import com.lyessou.authentification.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    //methode pour consulter la liste des taches
    @GetMapping("/tasks")
    public List<Task> listTasks(){
        return taskRepository.findAll();
    }

    //methode pour rajouter une tache
    @PostMapping("/tasks")
    public Task save(@RequestBody Task t){
        return taskRepository.save(t);
    }

}
