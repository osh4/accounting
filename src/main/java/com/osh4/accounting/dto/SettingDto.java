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
	private String key;
	private String type;
	@NotNull
	private String value;
}
