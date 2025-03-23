package ru.spring_pet_project.spring_todo.repositoryes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.spring_pet_project.spring_todo.entities.Task;
import ru.spring_pet_project.spring_todo.entities.entitiesSupport.Status;

import java.util.List;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long>, CrudRepository<Task, Long> {
    public List<Task> findByStatus(Status status);
}
