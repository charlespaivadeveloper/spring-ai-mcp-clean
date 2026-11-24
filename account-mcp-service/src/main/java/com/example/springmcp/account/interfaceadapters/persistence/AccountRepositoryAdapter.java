package com.example.springmcp.account.interfaceadapters.persistence;

import com.example.springmcp.account.entities.Account;
import com.example.springmcp.account.entities.PersonId;
import com.example.springmcp.account.usecases.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountSpringDataRepository springDataRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public List<Account> findByPersonId(final PersonId personId) {
        log.debug("Fetching accounts for personId={}", personId.value());
        return mapper.toDomainList(
                springDataRepository.findByPersonId(personId.value())
        );
    }

    @Override
    public Account save(final Account account) {
        log.debug("Saving account number={}", account.getNumber());
        return mapper.toDomain(
                springDataRepository.save(mapper.toEntity(account))
        );
    }
}
