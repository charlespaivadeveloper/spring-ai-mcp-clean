package com.example.springmcp.person.usecases;

import com.example.springmcp.person.entities.PersonNotFoundException;
import com.example.springmcp.person.entities.PersonId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonQueryService implements FindPersonByIdUseCase, FindPersonsByNationalityUseCase {

    private final PersonRepositoryPort personRepository;

    @Override
    public PersonResponse findById(final Long id) {
        log.debug("Finding person by id={}", id);
        final PersonId personId = PersonId.of(id);
        return personRepository.findById(personId)
                .map(PersonResponseMapper::toResponse)
                .orElseThrow(() -> new PersonNotFoundException(personId));
    }

    @Override
    public List<PersonResponse> findByNationality(final String nationality) {
        log.debug("Finding persons by nationality={}", nationality);
        final List<PersonResponse> results = PersonResponseMapper.toResponseList(
                personRepository.findByNationality(nationality)
        );
        log.debug("Found {} persons with nationality={}", results.size(), nationality);
        return results;
    }
}
