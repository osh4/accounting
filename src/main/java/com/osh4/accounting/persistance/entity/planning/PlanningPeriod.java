package com.osh4.accounting.persistance.entity.planning;

import com.osh4.accounting.persistance.entity.AbstractEntity;
import com.osh4.accounting.persistance.entity.account.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "planning_periods")
public class PlanningPeriod extends AbstractEntity {

    @Column(nullable = false, name = "f_name")
    private String name;
    @Column(nullable = false, name = "f_description")
    private String description;
    @Column(nullable = false, name = "f_date_part")
    private Integer datePart;
    @Column(nullable = false, name = "f_count")
    private Integer count;

    @OneToMany(mappedBy = "period")
    private Set<Plan> plans;

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

    public Integer getDatePart() {
        return datePart;
    }

    public void setDatePart(Integer datePart) {
        this.datePart = datePart;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<Plan> getPlans() {
        return plans;
    }

    public void setPlans(Set<Plan> plans) {
        this.plans = plans;
    }
}
