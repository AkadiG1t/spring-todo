package ru.spring_pet_project.spring_todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.spring_pet_project.spring_todo.exceptions.EmptyRequestTaskException;
import ru.spring_pet_project.spring_todo.enums.Status;
import ru.spring_pet_project.spring_todo.model.TaskDTO;
import ru.spring_pet_project.spring_todo.exceptions.NotFoundTaskException;
import ru.spring_pet_project.spring_todo.service.TaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> saveTask(@RequestBody @Valid TaskDTO requestTask) {
            return ResponseEntity.ok(taskService.createTask(requestTask));
        }

   @PutMapping("/{id}")
   public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
   }

    @GetMapping("/filtered")
    public ResponseEntity<?> filteredTasksByStatus(@RequestParam String status) {
        try {
            return ResponseEntity.ok(taskService.filteredTaskByStatus(Status.valueOf(status.toUpperCase())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<TaskDTO>> sortedTasks(@RequestParam String date, @RequestParam String status) {
        return ResponseEntity.ok(taskService.sortedTask(date,status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Задача успешно удалена");
    }

    @ExceptionHandler(NotFoundTaskException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundTaskException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(EmptyRequestTaskException.class)
    public ResponseEntity<String> handleEmptyTaskException(EmptyRequestTaskException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handeValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

