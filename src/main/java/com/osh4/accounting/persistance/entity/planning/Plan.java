package com.osh4.accounting.persistance.entity.planning;

import com.osh4.accounting.persistance.entity.AbstractEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "financial_plans")
public class Plan extends AbstractEntity {
    @Column(nullable = false, name = "f_name")
    private String name;

    @Column(nullable = false, name = "f_first_day")
    private LocalDate firstDate;

    @Column(nullable = false, name = "f_money_limit")
    private BigDecimal moneyLimit;

    @ManyToOne
    @JoinColumn(name = "f_period", referencedColumnName = "pk")
    private PlanningPeriod period;

    @ManyToOne
    @JoinColumn(name = "f_parent", referencedColumnName = "pk")
    private Plan parent;

    @OneToMany(mappedBy = "plan")
    private Set<PlanEntry> entries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public BigDecimal getMoneyLimit() {
        return moneyLimit;
    }

    public void setMoneyLimit(BigDecimal moneyLimit) {
        this.moneyLimit = moneyLimit;
    }

    public PlanningPeriod getPeriod() {
        return period;
    }

    public void setPeriod(PlanningPeriod period) {
        this.period = period;
    }

    public Plan getParent() {
        return parent;
    }

    public void setParent(Plan parent) {
        this.parent = parent;
    }

    public Set<PlanEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<PlanEntry> entries) {
        this.entries = entries;
    }
}
