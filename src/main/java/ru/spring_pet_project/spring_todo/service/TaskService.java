package ru.spring_pet_project.spring_todo.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spring_pet_project.spring_todo.entity.Task;
import ru.spring_pet_project.spring_todo.enums.Status;
import ru.spring_pet_project.spring_todo.model.TaskDTO;
import ru.spring_pet_project.spring_todo.exceptions.*;
import ru.spring_pet_project.spring_todo.taskMapper.TaskMapper;
import ru.spring_pet_project.spring_todo.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskDTO createTask(TaskDTO requestTask) {
        Optional.ofNullable(requestTask).orElseThrow(()
                -> new EmptyRequestTaskException("Задача не может быть пустой"));

        Task task = repository.save(taskMapper.dtoToTask(requestTask));

        return taskMapper.taskToDTO(task);
    }

    @Transactional
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = repository.findAll();

        return tasks.stream()
                .map(taskMapper::taskToDTO)
                .toList();
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional.ofNullable(taskDTO).orElseThrow(()
                -> new EmptyRequestTaskException("Задача не может быть пустой"));

        Task updatedTask = repository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException("Задача с ID " + id + " не найдена"));

        Optional.ofNullable(taskDTO.getName())
                .ifPresent(updatedTask::setName);

        Optional.ofNullable(taskDTO.getDescription())
                .ifPresent(updatedTask::setDescription);

        Optional.ofNullable(taskDTO.getDueDate())
                .ifPresent(updatedTask::setDueDate);

        Optional.ofNullable(taskDTO.getStatus())
                        .ifPresent(updatedTask::setStatus);

        repository.save(updatedTask);

        return taskMapper.taskToDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundTaskException("Задача с ID " + id + " не найдена");
        }
        repository.deleteById(id);
    }

    @Transactional
    public List<TaskDTO> filteredTaskByStatus(Status status) {
        return repository.findByStatus(status).stream()
                .map(taskMapper::taskToDTO)
                .toList();
    }

    @Transactional
    public List<TaskDTO> sortedTask(String date, String status) {
        if (date == null && status == null) {
            throw new IllegalArgumentException("Поля для сортировки не должны быть пустыми");
        }

        if (date != null && status != null) {
           throw new MultipleSortingParametersException("К сожалениею, сортировка по двум параметрам пока не доступна");
        }

        List<TaskDTO> tasks = new ArrayList<>();

        if (date != null && date.equalsIgnoreCase("date")) {
           Sort sort = Sort.by(Sort.Direction.ASC, "dueDate");
           tasks = repository.findAll(sort).stream()
                   .map(taskMapper::taskToDTO)
                   .toList();
        }

        if (status != null && status.equalsIgnoreCase("status")) {
            Sort sort = Sort.by(Sort.Direction.ASC, "status");
            tasks = repository.findAll(sort).stream()
                    .map(taskMapper::taskToDTO)
                    .toList();
        }

        return tasks;
    }
}
