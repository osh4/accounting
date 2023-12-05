package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import org.mapstruct.Mapper;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {
    TransactionTypeDto toDto(TransactionType model);

    TransactionType toModel(TransactionTypeDto dto);

}
