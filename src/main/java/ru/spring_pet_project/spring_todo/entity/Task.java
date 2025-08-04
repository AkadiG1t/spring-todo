package ru.spring_pet_project.spring_todo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.spring_pet_project.spring_todo.enums.Status;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    Status status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public Task (String name, String description, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        status = Status.TODO;
    }
}
