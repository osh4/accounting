package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    List<Settings> findAllByGrp(String grp);

    Settings findByGrpAndKey(String grp, String key);
}
