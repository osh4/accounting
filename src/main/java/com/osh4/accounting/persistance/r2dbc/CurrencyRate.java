package com.osh4.accounting.persistance.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

import static java.util.Objects.isNull;

@Data
@Table("rates")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CurrencyRate implements Persistable<String> {
    @Id
    private String id;
    private LocalDate rateDate;
    private Double rate;
    private String sourceId;
    private String targetId;
    @Transient
    private boolean isNewEntity;

    @Override
    public boolean isNew() {
        return isNull(getId()) || this.isNewEntity;
    }

    public CurrencyRate setAsNew() {
        this.isNewEntity = true;
        return this;
    }
}
