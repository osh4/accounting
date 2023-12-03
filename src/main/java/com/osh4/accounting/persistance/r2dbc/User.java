package com.osh4.accounting.persistance.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Objects.isNull;

@Data
@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User implements Persistable<String> {
    @Id
    private String id;
    private String email;
    private String name;
    private String password;
    private Set<String> roles;
//    @CreatedDate
//    private LocalDateTime createdDate;
//
//    @LastModifiedDate
//    private LocalDateTime lastModifiedDate;
    @Transient
    private boolean isNewEntity;

    @Override
    public boolean isNew() {
        return isNull(getId()) || this.isNewEntity;
    }

    public User setAsNew() {
        this.isNewEntity = true;
        return this;
    }
}
