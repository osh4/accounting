package com.osh4.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountDto {
    private String id;
    private String name;
    private String description;
    private CurrencyDto currency;
    private String userId;
}
