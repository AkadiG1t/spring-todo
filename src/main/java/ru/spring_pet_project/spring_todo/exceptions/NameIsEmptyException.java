package ru.spring_pet_project.spring_todo.exceptions;

public class NameIsEmptyException extends RuntimeException {
    public NameIsEmptyException(String message) {
        super(message);
    }
}
