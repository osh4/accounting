package com.osh4.accounting.persistance.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import static java.util.Objects.nonNull;

@Data
@Table("accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Account implements Persistable<String> {
    @Id
    private String id;
    private String name;
    private String description;
    private String currencyId;
    private String userId;

    @Override
    public boolean isNew() {
        return nonNull(id);
    }
}
