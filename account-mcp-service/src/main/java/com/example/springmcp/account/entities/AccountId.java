package com.example.springmcp.account.entities;

import java.util.Objects;

public record AccountId(Long value) {

    public AccountId {
        Objects.requireNonNull(value, "AccountId value must not be null");
    }

    public static AccountId of(final Long value) {
        return new AccountId(value);
    }
}
