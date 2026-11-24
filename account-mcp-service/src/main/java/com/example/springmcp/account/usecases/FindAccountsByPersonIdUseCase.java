package com.example.springmcp.account.usecases;

import java.util.List;

public interface FindAccountsByPersonIdUseCase {

    List<AccountResponse> findByPersonId(Long personId);
}
