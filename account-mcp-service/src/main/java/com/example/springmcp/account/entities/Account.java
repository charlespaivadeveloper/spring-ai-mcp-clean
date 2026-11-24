package com.example.springmcp.account.entities;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public final class Account {

    private final AccountId id;
    private final String number;
    private final int balance;
    private final PersonId personId;

    public static Account reconstitute(
            final AccountId id,
            final String number,
            final int balance,
            final PersonId personId) {

        Objects.requireNonNull(id, "id must not be null");
        validateFields(number, balance, personId);
        return Account.builder()
                .id(id)
                .number(number)
                .balance(balance)
                .personId(personId)
                .build();
    }

    public static Account create(
            final String number,
            final int balance,
            final PersonId personId) {

        validateFields(number, balance, personId);
        return Account.builder()
                .number(number)
                .balance(balance)
                .personId(personId)
                .build();
    }

    public boolean hasFunds() {
        return balance > 0;
    }

    private static void validateFields(
            final String number,
            final int balance,
            final PersonId personId) {

        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("account number must not be blank");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("balance must not be negative");
        }
        Objects.requireNonNull(personId, "personId must not be null");
    }
}
