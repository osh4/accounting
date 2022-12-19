package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
}
