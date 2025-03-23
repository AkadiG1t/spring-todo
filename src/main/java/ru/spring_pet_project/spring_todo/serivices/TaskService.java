package ru.spring_pet_project.spring_todo.serivices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.spring_pet_project.spring_todo.entities.Task;
import ru.spring_pet_project.spring_todo.entities.entitiesSupport.Status;
import ru.spring_pet_project.spring_todo.exceptions.*;
import ru.spring_pet_project.spring_todo.repositoryes.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository repository;

    @Transactional(rollbackFor = {NameIsNullException.class})
    public Task createTask(Task task) throws NameIsNullException {
        log.info("Попытка создать задачу с именем: {}", task.getName());

        if (task == null) {
            throw new TaskIsNullException("Похоже, вы забыли ввести данные для создания задачи");
        }

        if (task.getName() == null) {
            throw new NameIsNullException("Имя не может быть пустым");
        }

        Task savedTask = new Task(task.getName(), task.getDescription(), task.getDueDate());
        repository.save(savedTask);

        log.info("Задача успешно создана с ID: {}", savedTask.getId());

        return savedTask;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Task> getAllTasks() {
        log.info("Попытка получить список всех задач");

        List<Task> tasks = new ArrayList<>();
        repository.findAll().
                forEach(tasks::add);

        return tasks;
    }

    @Transactional(rollbackFor = {NotFoundTaskException.class})
    public Task updateTask(Long id, Task task)
            throws NotFoundTaskException {

        log.info("Попытка обновить задачу с ID {}", id);

        if (task == null) {
            throw new TaskIsNullException("Похоже, что вы забыли внести данные для обновления задачи");
        }

        Task updatedTask = checkTask(id);

        if (task.getName() != null) {
            updatedTask.setName(task.getName());
        }

        if (task.getDescription() != null) {
            updatedTask.setDescription(task.getDescription());
        }

        if (task.getDueDate() != null) {
            updatedTask.setDueDate(task.getDueDate());
        }

        if (task.getStatus() != null) {
            checkStatus(task.getStatus().toString());

            updatedTask.setStatus(task.getStatus());
        }

        log.info("Задача успешно обновлена");

        return repository.save(updatedTask);
    }

    @Transactional(rollbackFor = {NotFoundTaskException.class})
    public String deleteTask(Long id) throws NotFoundTaskException {
        log.info("Попытка удалить задачу с ID {}", id);

        Task task = checkTask(id);

        repository.delete(task);

        log.info("Задача успешно удалена");

        return "Задача с ID " + id + " была успешно удалена";
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Task> filteredTaskByStatus(String status) {
        log.info("Попытка получить список отфильтрованных задач по статусу: {}", status);

        Status enumStatus = checkStatus(status);;

        List<Task> filteredTasks = repository.findByStatus(enumStatus);

        log.info("Задачи успешно отфильтрованы");

        return filteredTasks;

    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Task> sortedTask(String date, String status) throws NotFoundTaskException {
        log.info("Попытка получить получить отсортированный список задач");

        if (date == null && status == null) {
            throw new IllegalArgumentException("Поля для сортировки не должны быть пустыми");
        }

        if (date != null && status != null) {
           throw new MultipleSortingParametersException("К сожалениею сортировка по двум параметрам пока не доступна");
        }

        List<Task> tasks = new ArrayList<>();

        if (date != null && date.equalsIgnoreCase("date")) {
           log.info("Сортировка задач по дате");

           Sort sort = Sort.by(Sort.Direction.ASC, "dueDate");
           repository.findAll(sort)
                   .forEach(tasks::add);
        }

        if (status != null && status.equalsIgnoreCase("status")) {
            log.info("Сортировка задач по статусу");

            Sort sort = Sort.by(Sort.Direction.ASC, "status");
            repository.findAll(sort)
                    .forEach(tasks::add);
        }

        log.info("Успешный вывод отсортированного списка");

        return tasks;
    }

    private Task checkTask(Long id) throws NotFoundTaskException {
        return repository.findById(id).orElseThrow(() -> new NotFoundTaskException("Такой задачи нет в списке"));
    }

    private Status checkStatus(String status) {
        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadStatusException("Неверный статус: " + status);
        }
    }
}
