package com.example.springmcp.account.usecases;

import com.example.springmcp.account.entities.Account;
import com.example.springmcp.account.entities.AccountId;
import com.example.springmcp.account.entities.PersonId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountQueryService")
class AccountQueryServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private AccountQueryService accountQueryService;

    @Nested
    @DisplayName("findByPersonId")
    class FindByPersonId {

        @Test
        @DisplayName("should return all accounts belonging to a person")
        void shouldReturnAccountsForPerson() {
            final Long personId = 5L;
            final PersonId pid = PersonId.of(personId);
            final List<Account> accounts = List.of(
                    Account.reconstitute(AccountId.of(1L), "ACC001", 1000, pid),
                    Account.reconstitute(AccountId.of(2L), "ACC002", 500,  pid)
            );
            given(accountRepository.findByPersonId(pid)).willReturn(accounts);

            final List<AccountResponse> results = accountQueryService.findByPersonId(personId);

            assertThat(results).hasSize(2);
            assertThat(results).extracting(AccountResponse::personId).containsOnly(personId);
            assertThat(results).extracting(AccountResponse::hasFunds).containsOnly(true);
            then(accountRepository).should().findByPersonId(pid);
        }

        @Test
        @DisplayName("should return empty list when person has no accounts")
        void shouldReturnEmptyListWhenNoAccounts() {
            given(accountRepository.findByPersonId(PersonId.of(999L))).willReturn(List.of());

            assertThat(accountQueryService.findByPersonId(999L)).isEmpty();
        }

        @Test
        @DisplayName("should map hasFunds correctly for zero balance account")
        void shouldMapHasFundsForZeroBalance() {
            final PersonId pid = PersonId.of(1L);
            given(accountRepository.findByPersonId(pid)).willReturn(
                    List.of(Account.reconstitute(AccountId.of(1L), "ACC001", 0, pid))
            );

            final List<AccountResponse> results = accountQueryService.findByPersonId(1L);

            assertThat(results).hasSize(1);
            assertThat(results.get(0).hasFunds()).isFalse();
        }
    }
}
