package com.osh4.accounting.dto.money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private String isocode;
    private String name;
    private Set<CurrencyRateDto> rates;
}
