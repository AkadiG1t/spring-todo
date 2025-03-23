package ru.spring_pet_project.spring_todo.exceptions;

public class TaskIsNullException extends RuntimeException {
    public TaskIsNullException(String message) {
        super(message);
    }
}
