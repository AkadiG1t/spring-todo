package ru.spring_pet_project.spring_todo.exceptions;

public class NotFoundTaskException extends Exception {
    public NotFoundTaskException(String message) {
        super(message);
    }
}
