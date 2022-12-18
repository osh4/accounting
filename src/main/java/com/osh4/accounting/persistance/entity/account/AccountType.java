package com.osh4.accounting.persistance.entity.account;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.osh4.accounting.persistance.entity.AbstractEntity;

@Entity
@Table(name = "account_types")
public class AccountType extends AbstractEntity {
    @ElementCollection
    @CollectionTable(name = "i18naccounttypes", foreignKey = @ForeignKey(name = "none"), joinColumns = @JoinColumn(name = "pk"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"locale", "f_name"})
            })
    @MapKeyColumn(name = "locale", length = 4)
    @Column(nullable = false, unique = true, name = "f_name")
    @NotEmpty
    private Map<Locale, String> name = new HashMap<>();

    @OneToMany(mappedBy = "accountType")
    private Set<Account> account;

    public Map<Locale, String> getName() {
        return name;
    }

    public void setName(Map<Locale, String> name) {
        this.name = name;
    }

    public Set<Account> getAccount() {
        return account;
    }

    public void setAccount(Set<Account> account) {
        this.account = account;
    }
}
