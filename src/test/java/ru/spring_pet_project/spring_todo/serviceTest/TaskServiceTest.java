package ru.spring_pet_project.spring_todo.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spring_pet_project.spring_todo.model.TaskDTO;
import ru.spring_pet_project.spring_todo.entity.Task;
import ru.spring_pet_project.spring_todo.enums.Status;
import ru.spring_pet_project.spring_todo.exceptions.MultipleSortingParametersException;
import ru.spring_pet_project.spring_todo.exceptions.NotFoundTaskException;
import ru.spring_pet_project.spring_todo.taskMapper.TaskMapper;
import ru.spring_pet_project.spring_todo.repository.TaskRepository;
import ru.spring_pet_project.spring_todo.service.TaskService;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static ru.spring_pet_project.spring_todo.enums.Status.IN_PROGRESS;
import static ru.spring_pet_project.spring_todo.enums.Status.TODO;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskService taskService;
    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setStatus(Status.TODO);

        taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setStatus(task.getStatus());
    }

    @Test
    void createTask_ShouldReturnTaskDTO() {
        when(repository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO.getName(), result.getName());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTaskDTO() {
        when(repository.findAll()).thenReturn(Collections.singletonList(task));
        when(taskMapper.taskToDTO(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskDTO, result.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateTask_ShouldReturnUpdatedTaskDTO() {
        Long taskId = 1L;

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.taskToDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.updateTask(taskId, taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO.getName(), result.getName());
        assertEquals(taskDTO.getDescription(), result.getDescription());
        assertEquals(taskDTO.getDueDate(), result.getDueDate());
        assertEquals(taskDTO.getStatus(), result.getStatus());

        verify(repository, times(1)).findById(taskId);
        verify(repository, times(1)).save(task);
        verify(taskMapper, times(1)).taskToDTO(task);
    }


    @Test
    void deleteTask_ShouldDeleteTask() {
        Long taskId = 1L;

        when(repository.existsById(taskId)).thenReturn(true);

        taskService.deleteTask(taskId);

        verify(repository,times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        Long taskId = 1L;

        when(repository.existsById(taskId)).thenReturn(false);

        assertThrows(NotFoundTaskException.class, () -> taskService.deleteTask(taskId));
        verify(repository, never()).deleteById(taskId);
    }

    @Test
    void filteredTaskByStatus_ShouldReturnFilteredTasks() {
        when(repository.findAll()).thenReturn(Collections.singletonList(task));
        when(taskMapper.taskToDTO(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.filteredTaskByStatus(TODO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskDTO, result.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void sortedTask_ShouldReturnSortedTasksByDate() {
        String date = "date";

        when(repository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(task));
        when(taskMapper.taskToDTO(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.sortedTask(date, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskDTO, result.get(0));
        verify(repository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void sortedTask_ShouldThrowException_WhenMultipleSortingParameters() {
        String date = "date";
        String status = "status";

        assertThrows(MultipleSortingParametersException.class, () -> taskService.sortedTask(date, status));
        verify(repository, never()).findAll(any(Sort.class));
    }
}
