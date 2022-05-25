package com.osh4.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Getter
@Setter
@NoArgsConstructor
public class SettingsDto
{
	private String group;
	private String key;
	private String type;
	private String value;
}
