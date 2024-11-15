package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.TransactionCategoryDto;
import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import org.mapstruct.Mapper;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface TransactionCategoryMapper {
    TransactionCategoryDto toDto(TransactionCategory model);

    TransactionCategory toModel(TransactionCategoryDto dto);

}
