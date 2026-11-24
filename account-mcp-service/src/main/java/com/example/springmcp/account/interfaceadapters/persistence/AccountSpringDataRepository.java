package com.example.springmcp.account.interfaceadapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountSpringDataRepository extends JpaRepository<AccountJpaEntity, Long> {

    List<AccountJpaEntity> findByPersonId(Long personId);
}
