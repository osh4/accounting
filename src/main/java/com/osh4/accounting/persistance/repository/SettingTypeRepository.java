package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.SettingType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingTypeRepository extends ReactiveCrudRepository<SettingType, String> {

}
