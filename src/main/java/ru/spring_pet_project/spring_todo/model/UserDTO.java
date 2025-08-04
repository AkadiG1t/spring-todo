package ru.spring_pet_project.spring_todo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserDTO {
    Long id;
    String firstName;
    String lastName;
    List<TaskDTO> userTasks;
}
