package com.osh4.accounting.service.impl;

import com.osh4.accounting.persistance.entity.money.Currency;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(final CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency getCurrencyByIsoCode(String isocode) {
        return currencyRepository.findByIsocode(isocode);
    }
}
