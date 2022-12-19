package com.osh4.accounting.persistance.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.nonNull;

@Data
@Table("transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Transaction implements Persistable<Long> {
    @Id
    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private String description;
    private String transactionTypeId;
    private String sourceAccountId;
    private String targetAccountId;

    @Override
    public boolean isNew() {
        return nonNull(id);
    }
}