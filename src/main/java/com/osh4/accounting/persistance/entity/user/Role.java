package com.osh4.accounting.persistance.entity.user;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import com.osh4.accounting.persistance.entity.AbstractEntity;

@Entity
@Table(name = "user_roles")
@Setter
@Getter
public class Role extends AbstractEntity implements GrantedAuthority {
    @Column(nullable = false, unique = true, name = "f_name")
    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return Objects.equals(pk, role.pk);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
