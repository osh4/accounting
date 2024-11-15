package com.osh4.accounting.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
public class AccountDto {
    private String id;
    private String name;
    private String description;
    private CurrencyDto currency;
    private UserDto user;
    private BigDecimal amount;
}
