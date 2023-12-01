package com.osh4.accounting.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface PaginatedSearchService {

    PageRequest paginationInfo(int page, int size, String sort);
    Sort sortFromField(String sortField);
}
