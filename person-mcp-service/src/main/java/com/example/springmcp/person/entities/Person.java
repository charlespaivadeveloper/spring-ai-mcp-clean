package com.example.springmcp.person.entities;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public final class Person {

    private final PersonId id;
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String nationality;
    private final Gender gender;

    public static Person reconstitute(
            final PersonId id,
            final String firstName,
            final String lastName,
            final int age,
            final String nationality,
            final Gender gender) {

        Objects.requireNonNull(id, "id must not be null");
        validateFields(firstName, lastName, age, nationality);
        return Person.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .nationality(nationality)
                .gender(gender)
                .build();
    }

    public static Person create(
            final String firstName,
            final String lastName,
            final int age,
            final String nationality,
            final Gender gender) {

        validateFields(firstName, lastName, age, nationality);
        return Person.builder()
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .nationality(nationality)
                .gender(gender)
                .build();
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdult() {
        final int adultAge = 18;
        return age >= adultAge;
    }

    private static void validateFields(
            final String firstName,
            final String lastName,
            final int age,
            final String nationality) {

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName must not be blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName must not be blank");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("age must be positive");
        }
        if (nationality == null || nationality.isBlank()) {
            throw new IllegalArgumentException("nationality must not be blank");
        }
    }
}
