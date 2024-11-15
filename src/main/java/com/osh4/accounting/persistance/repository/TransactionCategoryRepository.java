package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionCategoryRepository extends ReactiveCrudRepository<TransactionCategory, String> {
}
