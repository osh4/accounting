package com.osh4.accounting.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.osh4.accounting.converters.impl.AccountConverter;
import com.osh4.accounting.dto.account.AccountDto;
import com.osh4.accounting.persistance.entity.account.AccountType;
import com.osh4.accounting.persistance.entity.user.Role;
import com.osh4.accounting.persistance.repository.*;
import com.osh4.accounting.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.osh4.accounting.persistance.entity.user.User;
import com.osh4.accounting.persistance.entity.account.Account;
import com.osh4.accounting.persistance.entity.money.Currency;

import static com.osh4.accounting.utils.Constants.Roles.ROLE_USER;
import static com.osh4.accounting.utils.Constants.ru;

/**
 * Account service.
 *
 * @author osh4 <kosntantin@osh4.com>
 * @package com.osh4.accounting.service
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountConverter accountConverter;
    private final AccountTypeRepository accountTypeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public AccountServiceImpl(final AccountRepository accountRepository, final UserRepository userRepository,
                              final CurrencyRepository currencyRepository, final AccountConverter accountConverter,
                              AccountTypeRepository accountTypeRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.accountConverter = accountConverter;
        this.accountTypeRepository = accountTypeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;

        createTestData();
    }

    private void createTestData() {
        final User user1 = createTestUser();
        final Currency currency = createTestCurrency();
        createTestAccount(user1, currency);
    }

    private User createTestUser() {
        final User user1 = new User();
        user1.setFirstName("test1");
        user1.setLastName("testtest1");
        user1.setLogin("testuser1");
        user1.setEnabled(true);
        user1.setRoles(Collections.singleton(createRole()));
        user1.setPassword(bCryptPasswordEncoder.encode("blabla"));
        this.userRepository.deleteAll();
        this.userRepository.save(user1);
        return user1;
    }

    private Role createRole() {
        final Role role = new Role();
        role.setName(ROLE_USER);
        roleRepository.save(role);
        return role;
    }

    private void createTestAccount(User user1, Currency currency) {
        final Account acc1 = new Account();
        acc1.getName().put(Locale.ENGLISH, "account1");
        acc1.getName().put(ru, "аккаунт1");

        acc1.setUser(user1);
        acc1.setCurrency(currency);
        acc1.setAccountType(createTestAccType());
        this.accountRepository.deleteAll();
        this.accountRepository.save(acc1);
    }

    private AccountType createTestAccType() {
        AccountType accType1 = new AccountType();
        accType1.getName().put(Locale.ENGLISH, "cash");
        accType1.getName().put(ru, "Наличные");
        this.accountTypeRepository.deleteAll();
        this.accountTypeRepository.save(accType1);
        return accType1;
    }

    private Currency createTestCurrency() {
        final Currency currency = new Currency();
        //currency.setFactor(1.0);
        currency.setIsocode("USD");
        currency.getName().put(Locale.ENGLISH, "dollar");
        currency.getName().put(ru, "доллар");

        //currency.setReverse_factor(0.1);
        this.currencyRepository.deleteAll();
        this.currencyRepository.save(currency);
        return currency;
    }

    @Override
    public List<AccountDto> getAllAccountsByUserCode(final String login) {
        final User user = userRepository.findByLogin(login);
        List<Account> accounts = accountRepository.findAllByUser(user);
        return accountConverter.convertAll(accounts);
    }

    @Override
    public List<AccountDto> getAllAccounts(final String login, final String accountTypeName) {
        final User user = userRepository.findByLogin(login);
        AccountType accountType = accountTypeRepository.findByName(accountTypeName);
        List<Account> accounts = accountRepository.findAllByUserAndAccountType(user, accountType);
        return accountConverter.convertAll(accounts);
    }

}
