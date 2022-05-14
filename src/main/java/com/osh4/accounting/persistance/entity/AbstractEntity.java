package com.osh4.accounting.persistance.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long pk;

	public long getPk()
	{
		return pk;
	}

	public void setPk(long pk)
	{
		this.pk = pk;
	}
}
