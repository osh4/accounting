package com.osh4.accounting.dto;

import lombok.*;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SettingDto
{
	private String key;
	private String type;
	private String value;
}
