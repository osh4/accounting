package com.osh4.accounting.persistance.entity.money;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.osh4.accounting.persistance.entity.AbstractEntity;

@Entity
@Table(name = "currency_rates")
public class CurrencyRate extends AbstractEntity
{
	@Column(nullable = false, name = "f_date")
	private LocalDate date;
	@Column(nullable = false, name = "f_rate")
	private Double rate;

	@ManyToOne
	@JoinColumn(name = "f_source")
	private Currency source;

	@ManyToOne(targetEntity = Currency.class)
	@JoinColumn(name = "f_target")
	private Currency target;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Currency getSource() {
		return source;
	}

	public void setSource(Currency source) {
		this.source = source;
	}

	public Currency getTarget() {
		return target;
	}

	public void setTarget(Currency target) {
		this.target = target;
	}
}
