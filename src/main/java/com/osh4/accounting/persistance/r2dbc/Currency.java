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
@Table("currencies")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Currency implements Persistable<String> {
    @Id
    private String id;
    private String isoCode;
    private String name;

    @Override
    public boolean isNew() {
        return nonNull(id);
    }
}
