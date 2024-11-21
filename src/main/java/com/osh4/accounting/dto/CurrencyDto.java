package com.osh4.accounting.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CurrencyDto {
    private String id;
    @NotBlank
    private String isoCode;
    @NotBlank
    private String name;
    private String longName;
}
