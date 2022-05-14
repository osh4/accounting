package com.osh4.accounting.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "settings")
public class Settings extends AbstractEntity {
    @Column(nullable = false, length = 100, name = "f_group")
    private String grp;
    @Column(nullable = false, length = 100, name = "f_key")
    private String key;
    @Column(nullable = false, length = 100, name = "f_type")
    private String type;
    @Column(name = "f_value")
    private String value;

    public String getGrp() {
        return grp;
    }

    public void setGrp(String group) {
        this.grp = group;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
