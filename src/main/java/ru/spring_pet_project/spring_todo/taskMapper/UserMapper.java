package ru.spring_pet_project.spring_todo.taskMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.spring_pet_project.spring_todo.entity.User;
import ru.spring_pet_project.spring_todo.model.UserDTO;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final TaskMapper taskMapper;

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());

        if (user.getUserTasks() != null) {
            userDTO.setUserTasks((user.getUserTasks().stream()
                    .map(taskMapper::taskToDTO)
                    .toList()));
        }

       return userDTO;
    }

    public User UserDTOtoUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setId(userDTO.getId());

        if (userDTO.getUserTasks() != null) {
            user.setUserTasks(userDTO.getUserTasks().stream()
                    .map(taskMapper::dtoToTask)
                    .toList());
        }

        return user;
    }
}
