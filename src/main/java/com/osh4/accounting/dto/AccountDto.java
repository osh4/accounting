package com.osh4.accounting.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountDto {
    private String id;
    private String name;
    private String description;
    private CurrencyDto currency;
    private UserDto user;
}
