package com.example.springmcp.person.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Person aggregate")
class PersonTest {

    @Nested
    @DisplayName("create factory method")
    class Create {

        @Test
        @DisplayName("should create a valid person when all fields are correct")
        void shouldCreatePersonWhenAllFieldsValid() {
            final Person person = Person.create("Alice", "Smith", 30, "USA", Gender.FEMALE);

            assertThat(person.getFirstName()).isEqualTo("Alice");
            assertThat(person.getLastName()).isEqualTo("Smith");
            assertThat(person.getAge()).isEqualTo(30);
            assertThat(person.getNationality()).isEqualTo("USA");
            assertThat(person.getGender()).isEqualTo(Gender.FEMALE);
            assertThat(person.getId()).isNull();
        }

        @Test
        @DisplayName("should throw when firstName is blank")
        void shouldThrowWhenFirstNameIsBlank() {
            assertThatThrownBy(() -> Person.create("", "Smith", 25, "Brazil", Gender.MALE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("firstName");
        }

        @Test
        @DisplayName("should throw when lastName is null")
        void shouldThrowWhenLastNameIsNull() {
            assertThatThrownBy(() -> Person.create("Alice", null, 25, "Brazil", Gender.FEMALE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("lastName");
        }

        @Test
        @DisplayName("should throw when age is zero or negative")
        void shouldThrowWhenAgeIsNotPositive() {
            assertThatThrownBy(() -> Person.create("Alice", "Smith", 0, "Brazil", Gender.FEMALE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("age");
        }

        @Test
        @DisplayName("should throw when nationality is blank")
        void shouldThrowWhenNationalityIsBlank() {
            assertThatThrownBy(() -> Person.create("Alice", "Smith", 25, "  ", Gender.FEMALE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nationality");
        }
    }

    @Nested
    @DisplayName("reconstitute factory method")
    class Reconstitute {

        @Test
        @DisplayName("should reconstitute person with all fields including id")
        void shouldReconstituteWithId() {
            final PersonId id = PersonId.of(1L);
            final Person person = Person.reconstitute(id, "Bob", "Doe", 40, "Germany", Gender.MALE);

            assertThat(person.getId()).isEqualTo(id);
            assertThat(person.getFirstName()).isEqualTo("Bob");
        }

        @Test
        @DisplayName("should throw when id is null during reconstitution")
        void shouldThrowWhenIdIsNull() {
            assertThatThrownBy(() -> Person.reconstitute(null, "Bob", "Doe", 40, "Germany", Gender.MALE))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("business methods")
    class BusinessMethods {

        @Test
        @DisplayName("fullName should concatenate firstName and lastName")
        void shouldReturnFullName() {
            final Person person = Person.create("Maria", "Garcia", 28, "Spain", Gender.FEMALE);
            assertThat(person.fullName()).isEqualTo("Maria Garcia");
        }

        @Test
        @DisplayName("isAdult should return true when age is 18 or more")
        void shouldReturnTrueForAdult() {
            final Person adult = Person.create("Alice", "Smith", 18, "UK", Gender.FEMALE);
            final Person senior = Person.create("Bob", "Jones", 65, "UK", Gender.MALE);

            assertThat(adult.isAdult()).isTrue();
            assertThat(senior.isAdult()).isTrue();
        }

        @Test
        @DisplayName("isAdult should return false when age is less than 18")
        void shouldReturnFalseForMinor() {
            final Person minor = Person.create("Charlie", "Brown", 17, "USA", Gender.MALE);
            assertThat(minor.isAdult()).isFalse();
        }
    }
}
