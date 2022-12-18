package com.osh4.accounting.persistance.entity.money;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.osh4.accounting.persistance.entity.AbstractEntity;
import com.osh4.accounting.persistance.entity.account.Account;
import com.osh4.accounting.persistance.entity.transactions.Transaction;
//import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "currencies")
public class Currency extends AbstractEntity {
    @Column(nullable = false, unique = true, length = 3, name = "f_isocode")
    private String isocode;

    @ElementCollection
    @CollectionTable(name = "i18ncurrencies", foreignKey = @ForeignKey(name = "none"), joinColumns = @JoinColumn(name = "pk"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"locale", "name"})
            })
    @MapKeyColumn(name = "locale", length = 4)
    @Column(nullable = false)
    @NotEmpty
    private Map<Locale, String> name = new HashMap<>();

//    @Column(nullable = false)
////	@NotEmpty
//    private Double factor;
//    //TODO: курсы должны быть на дату и храниться в отдельной таблице, плюс нужен метод выборки курсов на указываемую дату

//    private Double reverse_factor;

    @OneToMany(mappedBy = "currency")
    private Set<Account> accounts;

    @OneToMany(mappedBy = "currency")
    private Set<Transaction> transactions;

    @ManyToMany
    @JoinTable(name = "currencies_currency_rates",
            joinColumns = @JoinColumn(name = "currency_pk"),
            inverseJoinColumns = @JoinColumn(name = "currency_rates_pk"))
    private Set<CurrencyRate> currencyRates = new LinkedHashSet<>();

    public Set<CurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    public void setCurrencyRates(Set<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
    }

    public Currency() {
    }

    public String getIsocode() {
        return isocode;
    }

    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

//    public Double getFactor() {
//        return factor;
//    }
//
//    public void setFactor(Double factor) {
//        this.factor = factor;
//    }

    public Map<Locale, String> getName() {
        return name;
    }

    public void setName(Map<Locale, String> name) {
        this.name = name;
    }

//    public Double getReverse_factor() {
//        return Math.round(1.0 / this.factor * 100.0) / 100.0;
//    }
//
//    public void setReverse_factor(Double reverse_factor) {
//        this.reverse_factor = reverse_factor;
//    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
