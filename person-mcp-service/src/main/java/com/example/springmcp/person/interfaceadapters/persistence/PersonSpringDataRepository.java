package com.example.springmcp.person.interfaceadapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonSpringDataRepository extends JpaRepository<PersonJpaEntity, Long> {

    List<PersonJpaEntity> findByNationality(String nationality);
}
