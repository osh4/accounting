package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionMapper;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.persistance.repository.TransactionRepository;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionCategoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    private TransactionTypeRepository transactionTypeRepository;
    private TransactionCategoryService transactionCategoryService;
    private AccountRepository accountRepository;
    private TransactionMapper transactionMapper;


    @InjectMocks
    private TransactionServiceImpl service;

}