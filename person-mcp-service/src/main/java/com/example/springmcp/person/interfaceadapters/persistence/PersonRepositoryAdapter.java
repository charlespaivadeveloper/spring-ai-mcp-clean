package com.example.springmcp.person.interfaceadapters.persistence;

import com.example.springmcp.person.entities.Person;
import com.example.springmcp.person.entities.PersonId;
import com.example.springmcp.person.usecases.PersonRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonRepositoryAdapter implements PersonRepositoryPort {

    private final PersonSpringDataRepository springDataRepository;
    private final PersonPersistenceMapper mapper;

    @Override
    public Optional<Person> findById(final PersonId id) {
        log.debug("Fetching person entity with id={}", id.value());
        return springDataRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Person> findByNationality(final String nationality) {
        log.debug("Fetching person entities with nationality={}", nationality);
        return mapper.toDomainList(
                springDataRepository.findByNationality(nationality)
        );
    }

    @Override
    public Person save(final Person person) {
        log.debug("Saving person: {}", person.fullName());
        final var entity = mapper.toEntity(person);
        final var saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
