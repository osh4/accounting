package com.osh4.accounting.dto.money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateDto {
    private CurrencyDto source;
    private CurrencyDto target;
    private LocalDate date;
    private BigDecimal value;

}
