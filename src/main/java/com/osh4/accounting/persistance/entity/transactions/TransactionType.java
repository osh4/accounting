package com.osh4.accounting.persistance.entity.transactions;

import com.osh4.accounting.persistance.entity.AbstractEntity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "transaction_types", uniqueConstraints = {
        @UniqueConstraint(name = "uc_transactiontype_f_name", columnNames = {"f_name"})
})
public class TransactionType extends AbstractEntity {
    @Column(nullable = false, name = "f_name")
    private String name;
    @Column(name = "f_description")
    private String description;

    @OneToMany(mappedBy = "transactionType", orphanRemoval = true)
    private Set<Transaction> transactions = new LinkedHashSet<>();

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
