package com.example.springmcp.person.usecases;

import com.example.springmcp.person.entities.Person;
import com.example.springmcp.person.entities.PersonId;

import java.util.List;
import java.util.Optional;

public interface PersonRepositoryPort {

    Optional<Person> findById(PersonId id);

    List<Person> findByNationality(String nationality);

    Person save(Person person);
}
