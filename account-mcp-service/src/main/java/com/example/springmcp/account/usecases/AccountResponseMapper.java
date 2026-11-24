package com.example.springmcp.account.usecases;

import com.example.springmcp.account.entities.Account;

import java.util.List;
import java.util.stream.Collectors;

public final class AccountResponseMapper {

    private AccountResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static AccountResponse toResponse(final Account account) {
        return new AccountResponse(
                account.getId() != null ? account.getId().value() : null,
                account.getNumber(),
                account.getBalance(),
                account.getPersonId() != null ? account.getPersonId().value() : null,
                account.hasFunds()
        );
    }

    public static List<AccountResponse> toResponseList(final List<Account> accounts) {
        return accounts.stream()
                .map(AccountResponseMapper::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }
}
