package ru.spring_pet_project.spring_todo.exceptions;

public class EmptyRequestTaskException extends RuntimeException {
    public EmptyRequestTaskException(String message) {
        super(message);
    }
}
