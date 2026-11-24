package com.example.springmcp.person.interfaceadapters.persistence;

import com.example.springmcp.person.entities.Gender;
import com.example.springmcp.person.entities.Person;
import com.example.springmcp.person.entities.PersonId;
import com.example.springmcp.person.usecases.PersonRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("PersonRepositoryAdapter Integration Tests")
class PersonRepositoryAdapterIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("persondb_test")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void configureDataSource(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.clean-disabled", () -> false);
    }

    @Autowired
    private PersonRepositoryPort personRepository;

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should find person by existing id")
        void shouldFindPersonByExistingId() {
            final Person saved = personRepository.save(
                    Person.create("Test", "User", 25, "TestLand", Gender.MALE));
            final PersonId savedId = saved.getId();

            final Optional<Person> found = personRepository.findById(savedId);

            assertThat(found).isPresent();
            assertThat(found.get().getFirstName()).isEqualTo("Test");
            assertThat(found.get().getId()).isEqualTo(savedId);
        }

        @Test
        @DisplayName("should return empty Optional when id does not exist")
        void shouldReturnEmptyWhenIdNotFound() {
            final Optional<Person> found = personRepository.findById(PersonId.of(Long.MAX_VALUE));
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByNationality")
    class FindByNationality {

        @Test
        @DisplayName("should return all persons with matching nationality")
        void shouldReturnPersonsWithMatchingNationality() {
            final String nationality = "UniqueTestLand";
            personRepository.save(Person.create("Alice", "A", 25, nationality, Gender.FEMALE));
            personRepository.save(Person.create("Bob",   "B", 28, nationality, Gender.MALE));
            personRepository.save(Person.create("Carol", "C", 30, "OtherLand", Gender.FEMALE));

            final List<Person> results = personRepository.findByNationality(nationality);

            assertThat(results).hasSize(2);
            assertThat(results).extracting(Person::getNationality).containsOnly(nationality);
        }

        @Test
        @DisplayName("should return empty list for unknown nationality")
        void shouldReturnEmptyListForUnknownNationality() {
            assertThat(personRepository.findByNationality("Atlantis")).isEmpty();
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("should persist a new person and assign an id")
        void shouldPersistNewPersonWithGeneratedId() {
            final Person saved = personRepository.save(
                    Person.create("Xavier", "Cruz", 33, "Portugal", Gender.MALE));

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getId().value()).isPositive();
            assertThat(saved.getFirstName()).isEqualTo("Xavier");
        }
    }
}
