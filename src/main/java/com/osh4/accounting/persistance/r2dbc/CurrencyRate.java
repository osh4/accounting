package com.osh4.accounting.persistance.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

import static java.util.Objects.nonNull;

@Data
@Table("rates")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CurrencyRate implements Persistable<String> {
    @Id
    private String id;
    private LocalDate date;
    private Double rate;
    private String sourceId;
    private String targetId;

    @Override
    public boolean isNew() {
        return nonNull(id);
    }
}
