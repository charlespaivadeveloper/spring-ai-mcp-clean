package com.example.springmcp.person.interfaceadapters.persistence;

import com.example.springmcp.person.entities.Person;
import com.example.springmcp.person.entities.PersonId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface PersonPersistenceMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "longToPersonId")
    Person toDomain(PersonJpaEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "personIdToLong")
    PersonJpaEntity toEntity(Person person);

    List<Person> toDomainList(List<PersonJpaEntity> entities);

    @Named("longToPersonId")
    default PersonId longToPersonId(final Long id) {
        return id == null ? null : PersonId.of(id);
    }

    @Named("personIdToLong")
    default Long personIdToLong(final PersonId personId) {
        return personId == null ? null : personId.value();
    }
}
