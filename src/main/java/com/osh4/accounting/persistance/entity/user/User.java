package com.osh4.accounting.persistance.entity.user;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.osh4.accounting.persistance.entity.AbstractEntity;
import com.osh4.accounting.persistance.entity.account.Account;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User extends AbstractEntity implements UserDetails {

    @Column(nullable = false, unique = true, name = "f_login")
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String login;
    @Column(name = "f_firstname")
    private String firstName;
    @Column(name = "f_lastname")
    private String lastName;
    @Column(nullable = false, name = "f_enabled")
    private Boolean enabled;
    @Column(nullable = false, name = "f_password")
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String password;

    @Transient
    private String passwordConfirm;

    @OneToMany(mappedBy = "user")
    private Set<Account> accounts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_pk"),
            inverseJoinColumns = @JoinColumn(name = "roles_pk"))
    private Set<Role> roles = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }
}
