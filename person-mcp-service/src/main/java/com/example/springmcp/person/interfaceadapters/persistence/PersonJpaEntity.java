package com.example.springmcp.person.interfaceadapters.persistence;

import com.example.springmcp.person.entities.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, length = 100)
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
}
