package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.entity.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long>
{
	AccountType findByName(final String name);

}
