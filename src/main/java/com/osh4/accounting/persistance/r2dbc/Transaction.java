package com.osh4.accounting.persistance.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.isNull;

@Data
@Table("transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Transaction implements Persistable<String> {
    @Id
    private String id;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String description;
    private String transactionTypeId;
    @Transient
    private TransactionType transactionType;
    @Transient
    private Account sourceAccount;
    private String sourceAccountId;
    @Transient
    private Account targetAccount;
    private String targetAccountId;
    @Transient
    private boolean isNewEntity;

    @Override
    public boolean isNew() {
        return isNull(getId()) || this.isNewEntity;
    }

    public Transaction setAsNew() {
        this.isNewEntity = true;
        return this;
    }
}
