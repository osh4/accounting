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
@Table("settings")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Setting implements Persistable<String> {
    @Id
    private String key;
    private String type;
    private String typeJavaClass;
    private String value;

    @Override
    public String getId() {
        return getKey();
    }

    @Override
    public boolean isNew() {
        return nonNull(key);
    }
}