package com.osh4.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SettingTypeDto {
    @NotNull(message = "name cannot be null")
    private String name;
    @NotNull(message = "className cannot be null")
    private String className;
}
