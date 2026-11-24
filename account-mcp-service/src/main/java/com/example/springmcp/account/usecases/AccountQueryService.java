package com.example.springmcp.account.usecases;

import com.example.springmcp.account.entities.PersonId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryService implements FindAccountsByPersonIdUseCase {

    private final AccountRepositoryPort accountRepository;

    @Override
    public List<AccountResponse> findByPersonId(final Long personId) {
        log.debug("Finding accounts for personId={}", personId);
        final var pid = PersonId.of(personId);
        final List<AccountResponse> results = AccountResponseMapper.toResponseList(
                accountRepository.findByPersonId(pid)
        );
        log.debug("Found {} accounts for personId={}", results.size(), personId);
        return results;
    }
}
