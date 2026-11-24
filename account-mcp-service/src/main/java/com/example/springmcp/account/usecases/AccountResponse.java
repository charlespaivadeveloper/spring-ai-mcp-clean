package com.example.springmcp.account.usecases;

public record AccountResponse(
        Long id,
        String number,
        int balance,
        Long personId,
        boolean hasFunds) {
}
