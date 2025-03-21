package ru.spring_pet_project.spring_todo.exceptions;

public class BadStatusException extends IllegalArgumentException {
    public BadStatusException(String message) {
        super(message);
    }
}
