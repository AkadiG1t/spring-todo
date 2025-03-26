package ru.spring_pet_project.spring_todo.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import ru.spring_pet_project.spring_todo.enums.Status;

import java.time.LocalDate;

@Data
public class TaskDTO {
    Long id;
    @NotEmpty(message = "Имя не может быть пустым")
    String name;
    String description;
    LocalDate dueDate;
    Status status;
}
