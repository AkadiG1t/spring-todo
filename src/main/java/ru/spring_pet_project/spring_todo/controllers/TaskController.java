package ru.spring_pet_project.spring_todo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spring_pet_project.spring_todo.entities.Task;
import ru.spring_pet_project.spring_todo.exceptions.BadStatusException;
import ru.spring_pet_project.spring_todo.exceptions.NameIsNullException;
import ru.spring_pet_project.spring_todo.exceptions.NotFoundTaskException;
import ru.spring_pet_project.spring_todo.exceptions.TaskIsNullException;
import ru.spring_pet_project.spring_todo.serivices.TaskService;


@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<?> saveTask(@RequestBody Task task) {
        try {
            return ResponseEntity.ok(taskService.createTask(task));
        } catch (NameIsNullException | TaskIsNullException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

   @PutMapping("/{id}")
   public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            return ResponseEntity.ok(taskService.updateTask(id, task));
        } catch (NotFoundTaskException | TaskIsNullException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
   }

    @GetMapping("/filtered")
    public ResponseEntity<?> filteredTasksByStatus(@RequestParam String status) {
        try {
            return ResponseEntity.ok(taskService.filteredTaskByStatus(status));
        } catch (BadStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/sorted")
    public ResponseEntity<?> sortedTasks(@RequestParam String date, @RequestParam String status) {
        try {
            return ResponseEntity.ok(taskService.sortedTask(date,status));
        } catch (NotFoundTaskException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.deleteTask(id));
        } catch (NotFoundTaskException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
