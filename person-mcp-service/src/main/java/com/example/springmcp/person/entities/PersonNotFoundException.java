package com.example.springmcp.person.entities;

public class PersonNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersonNotFoundException(final PersonId id) {
        super("Person not found with id: " + id.value());
    }

    public PersonNotFoundException(final Long id) {
        super("Person not found with id: " + id);
    }
}
