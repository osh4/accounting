package com.osh4.accounting.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SettingDto
{
	@NotNull(message = "key cannot be null")
	private String key;
	@NotNull(message = "type cannot be null")
	private String type;
	@NotNull(message = "value cannot be null")
	private String value;
}
