package com.osh4.accounting.service.impl;

import com.osh4.accounting.service.PaginatedSearchService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@AllArgsConstructor
public class PaginatedSearchServiceImpl implements PaginatedSearchService {

    @Override
    public PageRequest paginationInfo(int page, int size, String sort) {
        return PageRequest.of(page, size, this.sortFromField(sort));
    }

    @Override
    public Sort sortFromField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return Sort.unsorted();
        }
        if (!StringUtils.contains(sortField, '_')) {
            return Sort.by(sortField);
        }
        String[] sortParts = sortField.split("_");
        Sort sort = Sort.by(sortParts[0]);
        if (StringUtils.contains(sortParts[1], "desc")) {
            return sort.descending();
        }
        return sort.ascending();
    }
}
