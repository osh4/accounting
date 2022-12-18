package com.osh4.accounting.service;

import com.osh4.accounting.dto.account.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAllAccountsByUserCode(String login);

    List<AccountDto> getAllAccounts(String login, String accountTypeName);
}
