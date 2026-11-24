package com.example.springmcp.person.usecases;

import com.example.springmcp.person.entities.Person;

import java.util.List;
import java.util.stream.Collectors;

public final class PersonResponseMapper {

    private PersonResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PersonResponse toResponse(final Person person) {
        return new PersonResponse(
                person.getId() != null ? person.getId().value() : null,
                person.getFirstName(),
                person.getLastName(),
                person.fullName(),
                person.getAge(),
                person.getNationality(),
                person.getGender(),
                person.isAdult()
        );
    }

    public static List<PersonResponse> toResponseList(final List<Person> persons) {
        return persons.stream()
                .map(PersonResponseMapper::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }
}
