package com.example.springmcp.person.entities;

import java.util.Objects;

public record PersonId(Long value) {

    public PersonId {
        Objects.requireNonNull(value, "PersonId value must not be null");
    }

    public static PersonId of(final Long value) {
        return new PersonId(value);
    }
}
