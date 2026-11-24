package com.example.springmcp.person.usecases;

import com.example.springmcp.person.entities.Gender;
import com.example.springmcp.person.entities.Person;
import com.example.springmcp.person.entities.PersonId;
import com.example.springmcp.person.entities.PersonNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonQueryService")
class PersonQueryServiceTest {

    @Mock
    private PersonRepositoryPort personRepository;

    @InjectMocks
    private PersonQueryService personQueryService;

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return PersonResponse when person exists")
        void shouldReturnPersonResponseWhenFound() {
            final Long id = 1L;
            final Person person = Person.reconstitute(
                    PersonId.of(id), "Alice", "Smith", 30, "USA", Gender.FEMALE);
            given(personRepository.findById(PersonId.of(id))).willReturn(Optional.of(person));

            final PersonResponse response = personQueryService.findById(id);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(id);
            assertThat(response.firstName()).isEqualTo("Alice");
            assertThat(response.fullName()).isEqualTo("Alice Smith");
            assertThat(response.adult()).isTrue();
            then(personRepository).should().findById(PersonId.of(id));
        }

        @Test
        @DisplayName("should throw PersonNotFoundException when person does not exist")
        void shouldThrowNotFoundWhenPersonMissing() {
            given(personRepository.findById(any())).willReturn(Optional.empty());

            assertThatThrownBy(() -> personQueryService.findById(99L))
                    .isInstanceOf(PersonNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("findByNationality")
    class FindByNationality {

        @Test
        @DisplayName("should return list of persons matching nationality")
        void shouldReturnMatchingPersons() {
            final String nationality = "Brazil";
            final List<Person> domainPersons = List.of(
                    Person.reconstitute(PersonId.of(1L), "Lucas", "Silva", 32, nationality, Gender.MALE),
                    Person.reconstitute(PersonId.of(2L), "Ana",   "Costa", 27, nationality, Gender.FEMALE)
            );
            given(personRepository.findByNationality(nationality)).willReturn(domainPersons);

            final List<PersonResponse> results = personQueryService.findByNationality(nationality);

            assertThat(results).hasSize(2);
            assertThat(results).extracting(PersonResponse::nationality).containsOnly(nationality);
            then(personRepository).should().findByNationality(nationality);
        }

        @Test
        @DisplayName("should return empty list when no persons match nationality")
        void shouldReturnEmptyListWhenNoMatch() {
            given(personRepository.findByNationality("Antarctica")).willReturn(List.of());

            assertThat(personQueryService.findByNationality("Antarctica")).isEmpty();
        }
    }
}
