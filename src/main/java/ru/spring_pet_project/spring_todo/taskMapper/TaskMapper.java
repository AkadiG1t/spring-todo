    package ru.spring_pet_project.spring_todo.taskMapper;

    import org.springframework.stereotype.Component;
    import ru.spring_pet_project.spring_todo.model.TaskDTO;
    import ru.spring_pet_project.spring_todo.entity.Task;

    @Component
    public class TaskMapper {

        public TaskDTO taskToDTO(Task task) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setName(task.getName());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setDueDate(task.getDueDate());
            taskDTO.setStatus(task.getStatus());

            return taskDTO;
        }

        public Task dtoToTask(TaskDTO taskDTO) {
            Task task = new Task();
            task.setId(taskDTO.getId());
            task.setDescription(taskDTO.getDescription());
            task.setName(taskDTO.getName());
            task.setStatus(taskDTO.getStatus());
            task.setDueDate(taskDTO.getDueDate());

            return task;
        }
    }
