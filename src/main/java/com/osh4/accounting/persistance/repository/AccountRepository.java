package com.osh4.accounting.persistance.repository;

import java.util.List;

import com.osh4.accounting.persistance.entity.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osh4.accounting.persistance.entity.user.User;
import com.osh4.accounting.persistance.entity.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser(final User userCode);

    List<Account> findAllByUserAndAccountType(final User userCode, final AccountType accountType);

}
