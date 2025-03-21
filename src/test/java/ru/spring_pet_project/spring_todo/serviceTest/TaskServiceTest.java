package ru.spring_pet_project.spring_todo.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spring_pet_project.spring_todo.entities.Task;
import ru.spring_pet_project.spring_todo.entities.entitiesSupport.Status;
import ru.spring_pet_project.spring_todo.exceptions.BadStatusException;
import ru.spring_pet_project.spring_todo.exceptions.MultipleSortingParametersException;
import ru.spring_pet_project.spring_todo.exceptions.NameIsNullException;
import ru.spring_pet_project.spring_todo.exceptions.NotFoundTaskException;
import ru.spring_pet_project.spring_todo.repositoryes.TaskRepository;
import ru.spring_pet_project.spring_todo.serivices.TaskService;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository repository;
    @InjectMocks
    private TaskService taskService;
    private Task task;
    Task savedTask;

    @BeforeEach
    void setUp() {
        task = new Task("TestTask", "TestDesc", LocalDate.now());
        savedTask = new Task("TestTask", "TestDesc", LocalDate.now());
        savedTask.setId(1L);
    }

    @Test
    void testSuccessCreateTask() throws NameIsNullException {
        when(repository.save(any(Task.class))).thenReturn(savedTask);
        
        Task result = taskService.createTask(task);
        
        assertNotNull(result);
        assertEquals(1L,savedTask.getId());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTaskWhichCausesNameIsNullException() {
        task.setName(null);

        assertThrows(NameIsNullException.class, () -> taskService.createTask(task));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    void testSuccessGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(savedTask);

        when(repository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testSuccessUpdateTask() throws NotFoundTaskException {
        when(repository.findById(1L)).thenReturn(Optional.of(savedTask));
        when(repository.save(any(Task.class))).thenReturn(savedTask);

        Task updatedTask = taskService.updateTask(1L, task);

        assertNotNull(updatedTask);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskWhichCausesNotFoundTaskException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundTaskException.class, () -> taskService.updateTask(1L, task));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    void testSuccessDeleteTask() throws NotFoundTaskException {
        when(repository.findById(1L)).thenReturn(Optional.of(savedTask));

        String result = taskService.deleteTask(1L);

        assertEquals("Задача с ID 1 была успешно удалена", result);
        verify(repository, times(1)).delete(savedTask);

        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Task> deletedTask = repository.findById(1L);
        assertTrue(deletedTask.isEmpty());
    }

    @Test
    void testDeleteTaskWhichCausesNotFoundTaskException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundTaskException.class, () -> taskService.deleteTask(1L));
        verify(repository, never()).delete(any(Task.class));
    }

    @Test
    void testSuccessFilteredTaskByStatus() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(savedTask);

        when(repository.findByStatus(Status.TODO)).thenReturn(tasks);

        List<Task> result = taskService.filteredTaskByStatus("TODO");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByStatus(Status.TODO);
    }

    @Test
    void testFilteredTaskByStatusWhichCausesBadStatusException() {
        assertThrows(BadStatusException.class, () -> taskService.filteredTaskByStatus("INVALID_STATUS"));

        verify(repository, never()).findByStatus(any(Status.class));
    }

    @Test
    void testSuccessSortedTaskByDate() throws NotFoundTaskException {
        List<Task> tasks = new ArrayList<>();
        tasks.add(savedTask);

        when(repository.findAll(any(Sort.class))).thenReturn(tasks);

        List<Task> result = taskService.sortedTask("date", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testSuccessSortedTaskByStatus() throws NotFoundTaskException {
        List<Task> tasks = new ArrayList<>();
        tasks.add(savedTask);

        when(repository.findAll(any(Sort.class))).thenReturn(tasks);

        List<Task> result = taskService.sortedTask(null, "status");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testSortedTaskWhichCausesMultipleSortingParametersException() {
        assertThrows(MultipleSortingParametersException.class, () -> taskService
                .sortedTask("date", "status"));
        verify(repository, never()).findAll(any(Sort.class));
    }

    void testSortedTaskWhichCausesIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> taskService.sortedTask(null, null));
        verify(repository, never()).findAll(any(Sort.class));
    }
}
