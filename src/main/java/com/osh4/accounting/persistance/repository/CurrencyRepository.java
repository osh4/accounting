package com.osh4.accounting.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osh4.accounting.persistance.entity.money.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>
{
	Currency findByIsocode(String isocode);
}
