package com.osh4.accounting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osh4.accounting.persistance.entity.money.Currency;
import com.osh4.accounting.persistance.repository.CurrencyRepository;

import static com.osh4.accounting.utils.Constants.ru_RU;

/**
 * Currency service.
 *
 * @author osh4 <osh4@osh4.com>
 * @package com.osh4.accounting.service
 */
public interface CurrencyService {
    List<Currency> getAllCurrencies();

    Currency getCurrencyByIsoCode(String isocode);
}
