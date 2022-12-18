package com.osh4.accounting.persistance.entity.transactions;

import com.osh4.accounting.persistance.entity.AbstractEntity;
import com.osh4.accounting.persistance.entity.account.Account;
import com.osh4.accounting.persistance.entity.money.Currency;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction extends AbstractEntity {

    @Column(nullable = false, name = "f_date")
    private LocalDate date;
    @Column(nullable = false, name = "f_amount")
    private BigDecimal amount;
    @Column(name = "f_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "f_type", referencedColumnName = "pk")
    private TransactionType transactionType;
    @ManyToOne
    @JoinColumn(name = "f_currency", referencedColumnName = "pk")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "f_source_account", referencedColumnName = "pk")
    private Account sourceAccount;
    @ManyToOne
    @JoinColumn(name = "f_target_account", referencedColumnName = "pk")
    private Account targetAccount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(pk, that.pk);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
