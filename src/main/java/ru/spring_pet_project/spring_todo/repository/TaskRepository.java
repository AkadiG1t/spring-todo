package ru.spring_pet_project.spring_todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spring_pet_project.spring_todo.entity.Task;
import ru.spring_pet_project.spring_todo.enums.Status;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Status status);
    void deleteById(Long id);
}
