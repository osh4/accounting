package com.osh4.accounting.persistance.entity.account;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.osh4.accounting.persistance.entity.AbstractEntity;
import com.osh4.accounting.persistance.entity.transactions.Transaction;
import com.osh4.accounting.persistance.entity.user.User;
import com.osh4.accounting.persistance.entity.money.Currency;

@Entity
@Table(name = "accounts")
public class Account extends AbstractEntity
{
	@ElementCollection
	@CollectionTable(name = "i18naccounts", foreignKey = @ForeignKey(name = "none"), joinColumns = @JoinColumn(name = "pk"),
					uniqueConstraints = {
					@UniqueConstraint(columnNames = {"locale", "name"})
	})
	@MapKeyColumn(name = "locale", length = 4)
	@Column(nullable = false)
	@NotEmpty
	private Map<Locale, String> name = new HashMap<>();

	@ManyToOne
	@JoinColumn(name = "f_currency", referencedColumnName = "pk")
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "f_account_type", referencedColumnName = "pk")
	private AccountType accountType;

	@ManyToOne
	@JoinColumn(name = "f_user", referencedColumnName = "pk")
	private User user;

	@OneToMany(mappedBy = "sourceAccount")
	private Set<Transaction> transactions;

	public Map<Locale, String> getName()
	{
		return name;
	}

	public void setName(final Map<Locale, String> name)
	{
		this.name = name;
	}

	public Currency getCurrency()
	{
		return currency;
	}

	public void setCurrency(final Currency currency)
	{
		this.currency = currency;
	}

	public AccountType getAccountType()
	{
		return accountType;
	}

	public void setAccountType(final AccountType accountType)
	{
		this.accountType = accountType;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}
}
