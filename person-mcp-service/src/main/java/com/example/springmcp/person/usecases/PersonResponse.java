package com.example.springmcp.person.usecases;

import com.example.springmcp.person.entities.Gender;

public record PersonResponse(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        int age,
        String nationality,
        Gender gender,
        boolean adult) {
}
