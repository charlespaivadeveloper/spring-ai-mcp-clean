package com.example.springmcp.account.usecases;

import com.example.springmcp.account.entities.Account;
import com.example.springmcp.account.entities.PersonId;

import java.util.List;

public interface AccountRepositoryPort {

    List<Account> findByPersonId(PersonId personId);

    Account save(Account account);
}
