package com.osh4.accounting.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osh4.accounting.persistance.entity.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    List<Settings> findAll();

    Settings findByGrpAndKey(String grp, String key);
}
