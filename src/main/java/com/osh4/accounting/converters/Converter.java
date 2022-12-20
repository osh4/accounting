package com.osh4.accounting.converters;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface Converter<SOURCE, TARGET>
{
	TARGET convert(SOURCE source);

	default List<TARGET> convertAll(List<SOURCE> sourceList)
	{
		if (CollectionUtils.isEmpty(sourceList))
		{
			return Collections.emptyList();
		}
		return sourceList.stream().map(this::convert).collect(Collectors.toList());
	}
}
