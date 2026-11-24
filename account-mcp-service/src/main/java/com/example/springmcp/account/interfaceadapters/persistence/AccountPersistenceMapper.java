package com.example.springmcp.account.interfaceadapters.persistence;

import com.example.springmcp.account.entities.Account;
import com.example.springmcp.account.entities.AccountId;
import com.example.springmcp.account.entities.PersonId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface AccountPersistenceMapper {

    @Mapping(target = "id",       source = "id",       qualifiedByName = "longToAccountId")
    @Mapping(target = "personId", source = "personId", qualifiedByName = "longToPersonId")
    Account toDomain(AccountJpaEntity entity);

    @Mapping(target = "id",       source = "id",       qualifiedByName = "accountIdToLong")
    @Mapping(target = "personId", source = "personId", qualifiedByName = "personIdToLong")
    AccountJpaEntity toEntity(Account account);

    List<Account> toDomainList(List<AccountJpaEntity> entities);

    @Named("longToAccountId")
    default AccountId longToAccountId(final Long id) {
        return id == null ? null : AccountId.of(id);
    }

    @Named("accountIdToLong")
    default Long accountIdToLong(final AccountId accountId) {
        return accountId == null ? null : accountId.value();
    }

    @Named("longToPersonId")
    default PersonId longToPersonId(final Long id) {
        return id == null ? null : PersonId.of(id);
    }

    @Named("personIdToLong")
    default Long personIdToLong(final PersonId personId) {
        return personId == null ? null : personId.value();
    }
}
