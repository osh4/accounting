package com.osh4.accounting.converters;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

	default String generateId() {
		return UUID.randomUUID().toString();
	}

	default String createIdIfNeeded(String id){
		return isBlank(id) ? generateId() : id;
	}
}
