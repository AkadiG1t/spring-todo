package ru.spring_pet_project.spring_todo.exceptions;

public class NotFoundTaskException extends RuntimeException {
    public NotFoundTaskException(String message) {
        super(message);
    }
}
