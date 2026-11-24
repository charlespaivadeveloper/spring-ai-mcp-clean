package com.example.springmcp.account.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account aggregate")
class AccountTest {

    private static final PersonId PERSON_ID = PersonId.of(1L);

    @Nested
    @DisplayName("create factory method")
    class Create {

        @Test
        @DisplayName("should create a valid account with all required fields")
        void shouldCreateValidAccount() {
            final Account account = Account.create("ACC001", 1000, PERSON_ID);

            assertThat(account.getNumber()).isEqualTo("ACC001");
            assertThat(account.getBalance()).isEqualTo(1000);
            assertThat(account.getPersonId()).isEqualTo(PERSON_ID);
            assertThat(account.getId()).isNull();
        }

        @Test
        @DisplayName("should allow zero balance")
        void shouldAllowZeroBalance() {
            final Account account = Account.create("ACC002", 0, PERSON_ID);

            assertThat(account.getBalance()).isZero();
            assertThat(account.hasFunds()).isFalse();
        }

        @Test
        @DisplayName("should throw when number is blank")
        void shouldThrowWhenNumberIsBlank() {
            assertThatThrownBy(() -> Account.create("", 100, PERSON_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("account number");
        }

        @Test
        @DisplayName("should throw when balance is negative")
        void shouldThrowWhenBalanceIsNegative() {
            assertThatThrownBy(() -> Account.create("ACC003", -1, PERSON_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("balance");
        }

        @Test
        @DisplayName("should throw when personId is null")
        void shouldThrowWhenPersonIdIsNull() {
            assertThatThrownBy(() -> Account.create("ACC004", 500, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("reconstitute factory method")
    class Reconstitute {

        @Test
        @DisplayName("should reconstitute with all fields including id")
        void shouldReconstituteWithId() {
            final AccountId id = AccountId.of(42L);
            final Account account = Account.reconstitute(id, "ACC010", 5000, PERSON_ID);

            assertThat(account.getId()).isEqualTo(id);
            assertThat(account.getNumber()).isEqualTo("ACC010");
        }
    }

    @Nested
    @DisplayName("business methods")
    class BusinessMethods {

        @Test
        @DisplayName("hasFunds should return true for positive balance")
        void shouldReturnTrueForPositiveBalance() {
            assertThat(Account.create("ACC011", 1, PERSON_ID).hasFunds()).isTrue();
        }

        @Test
        @DisplayName("hasFunds should return false for zero balance")
        void shouldReturnFalseForZeroBalance() {
            assertThat(Account.create("ACC012", 0, PERSON_ID).hasFunds()).isFalse();
        }
    }
}
