package com.osh4.accounting.persistance.entity.category;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.osh4.accounting.persistance.entity.AbstractEntity;

@Entity
@Table(name = "categories")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Category extends AbstractEntity
{

}
