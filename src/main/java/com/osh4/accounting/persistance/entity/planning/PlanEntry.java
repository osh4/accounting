package com.osh4.accounting.persistance.entity.planning;

import com.osh4.accounting.persistance.entity.AbstractEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fp_entries")
public class PlanEntry extends AbstractEntity {

    @Column(nullable = false, name = "f_name")
    private String name;
    @Column(nullable = false, name = "f_description")
    private String description;
    @Column(nullable = false, name = "f_date")
    private LocalDate date;
    @Column(nullable = false, name = "f_amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "f_plan", referencedColumnName = "pk")
    private Plan plan;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
