package com.example.springmcp.account.interfaceadapters.persistence;

import com.example.springmcp.account.entities.Account;
import com.example.springmcp.account.entities.PersonId;
import com.example.springmcp.account.usecases.AccountRepositoryPort;
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

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("AccountRepositoryAdapter Integration Tests")
class AccountRepositoryAdapterIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("accountdb_test")
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
    private AccountRepositoryPort accountRepository;

    @Nested
    @DisplayName("findByPersonId")
    class FindByPersonId {

        @Test
        @DisplayName("should return all accounts for an existing person")
        void shouldReturnAccountsForExistingPerson() {
            final PersonId personId = PersonId.of(1L);

            final List<Account> accounts = accountRepository.findByPersonId(personId);

            assertThat(accounts).isNotEmpty();
            assertThat(accounts).allMatch(a -> a.getPersonId().equals(personId));
        }

        @Test
        @DisplayName("should return empty list for person with no accounts")
        void shouldReturnEmptyForPersonWithNoAccounts() {
            assertThat(accountRepository.findByPersonId(PersonId.of(Long.MAX_VALUE))).isEmpty();
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("should persist a new account and assign an id")
        void shouldPersistNewAccount() {
            final Account saved = accountRepository.save(
                    Account.create("NEW-ACC-001", 5000, PersonId.of(1L)));

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getId().value()).isPositive();
            assertThat(saved.getNumber()).isEqualTo("NEW-ACC-001");
            assertThat(saved.getBalance()).isEqualTo(5000);
        }
    }
}
